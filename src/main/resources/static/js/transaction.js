
document.addEventListener('DOMContentLoaded',  () => {

    const product_items = document.querySelectorAll('.product-item');
    const product_table_body = document.getElementById('product-table-body')
    const total_quantity_element = document.getElementById('total-quantity')
    const total_cost_element = document.getElementById('total-cost')
    let customer_pay = document.getElementById('customerPay')
    let total_quantity_current = document.getElementsByClassName('total-quantity-current')[0]

    let change_value = 0
    let customer_pay_value = 0

    customer_pay.addEventListener('keyup', () => {
        const total_cost = parseInt(document.getElementById('total-cost').textContent)
        customer_pay_value = parseInt(document.getElementById('customerPay').value)

        const changeToCustomer = document.getElementById('changeToCustomer')
        if(!customer_pay_value) {
            changeToCustomer.textContent = 0
            return
        }


        console.log('Customer pay: ' + customer_pay_value)
        change_value = customer_pay_value - total_cost
        console.log('Change to customer: ' + change_value)
        changeToCustomer.textContent = change_value
    })


    let selected_products = new Map()
    // Select product
    product_items.forEach(item => {
        item.addEventListener('click', () => {
            const productId = item.dataset.id;
            const productName = item.dataset.name
            const productPrice = item.dataset.price

            console.log(productId, productName, productPrice)
            addProduct({
                id: productId,
                name: productName,
                price: productPrice,
            })

        });

    });



    // Function to handle adding a product to the cart
    function addProduct(product) {
        const existing_product = selected_products.get(product.id)

        if (existing_product) {
            // If product already selected, increase the quantity
            existing_product.quantity += 1;
        } else {
            // If the product is not selected, add it
            selected_products.set(product.id,  {
                id: product.id,
                name: product.name,
                price: product.price,
                quantity: 1,
            })
        }

        // Save the selected products to localStorage
        localStorage.setItem('selected_products', JSON.stringify(Array.from(selected_products.entries())))

        updateSelectedProducts();
    }


    // Update products that customer selected to table
    function updateSelectedProducts() {
        product_table_body.innerHTML = ''

        selected_products.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.name}</td>
                <td> ${item.quantity}</td>
                <td>${item.price}</td>
                <td><button data-btn="${item.id}" class="desc-btn"><i class="fa-solid fa-trash-can"></i></button></td>`


            product_table_body.appendChild(tr)
        });

        const desc_btn = document.querySelectorAll('.desc-btn')
        desc_btn.forEach(btn => {
            btn.addEventListener('click', () => {
                const id = btn.getAttribute('data-btn')
                selected_products.get(id).quantity -= 1
                if(selected_products.get(id).quantity === 0) {
                    selected_products.delete(id)
                }
                localStorage.setItem('selected_products', JSON.stringify(Array.from(selected_products.entries())))

                if(selected_products.size === 0) {
                    localStorage.clear()
                }



                updateSelectedProducts()

            })

        })

        calTotal()
    }



    function calTotal() {
        let total_quantity = 0
        let total_cost = 0
        if(selected_products.size === 0) {
            total_quantity_element.textContent = 0
            total_cost_element.textContent = 0
            total_quantity_current.textContent = 0
        }
        else {
            selected_products.forEach(item => {
                total_quantity += item.quantity
                total_cost += item.price * item.quantity
                total_quantity_element.textContent = total_quantity
                total_cost_element.textContent = total_cost
                total_quantity_current.textContent = total_quantity
            })
        }



        return [total_quantity, total_cost]

    }


    // When complete the payment
    function completePayment() {
        // Clear the cart and localStorage
        selected_products = new Map()
        localStorage.removeItem('selected_products')
        updateSelectedProducts()

        // window.location.href='/transaction/processing'
    }

    // Attach click event listener to the "Checkout" button
    const completePaymentButton = document.getElementById('complete-payment');
    completePaymentButton.addEventListener('click', function (event) {
        // Prevent the default behavior of the anchor tag
        event.preventDefault();
        console.log('click')

        if(selected_products.size === 0) {
            alert('No product selected')
            return ;
        }

        if(customer_pay_value === 0 || !customer_pay_value) {
            alert('How much money did customer give ?')
            return
        }

        if(change_value < 0) {
            alert(`Customer didn't give enough money`)
            return
        }

        let sent_selected_data = []
        for (let value of selected_products.values()) {
            sent_selected_data.push(value)
        }
        console.log(sent_selected_data)
        const selected_data = Array.from(selected_products.entries())

        console.log(selected_products)
        const date = new Date()

        // const day = date.getDate()
        const day = Math.floor(Math.random() * 27) + 1
        console.log(day)
        const formatedDay = day.toString().padStart(2, '0')
        // const month = date.getMonth()  // because in js, month from (0, 11)
        const month = Math.floor(Math.random() * 11) + 1
        const formatedMonth = month.toString().padStart(2, '0')
        const year = date.getFullYear()

        const hour = date.getHours()
        const formatedHour = hour.toString().padStart(2, '0')
        console.log(formatedHour)
        // console.log("hellooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo")
        const minute = date.getMinutes()
        const formatedMinute = minute.toString().padStart(2, '0')
        const second = date.getSeconds()
        const formatedSecond = second.toString().padStart(2, '0')
        const order_date = `${year}-${formatedMonth}-${formatedDay}`
        // console.log("HELLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
        console.log(order_date)
        const order_time = `${formatedHour}:${formatedMinute}:${formatedSecond}`
        var saleData = {
            products: sent_selected_data,
            total_cost: calTotal()[1],
            total_quantity: calTotal()[0],
            date: order_date,
            time: order_time,
            amount_given_by_customer: customer_pay_value,
            change_to_customer: change_value
        }



        fetch('http://localhost:8080/transaction/processing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(saleData)
        })
            .then(res => res.json())
            .then(data => {
                console.log(data)
                if(data.code === 1) {
                    completePayment()
                    console.log("Oke")
                    window.location.href = 'http://localhost:8080/transaction/processing?complete=true'
                }
                else {
                    console.log(data.message)
                    alert(data.message)
                    console.log('Something went wrong');
                }
            })
            .catch(error => console.log(error))



        // Trigger the payment completion logic

        // if(selected_products.size === 0) {
        //     alert('No product selected')
        // }
        // else {
        //     completePayment()
        //     // window.location.href='/transaction/status'

        // }


    });

    // Load cart from localStorage on page load
    const stored_selected_products = localStorage.getItem('selected_products');
    if (stored_selected_products) {
        selected_products = new Map(JSON.parse(stored_selected_products))
        updateSelectedProducts()
    }

    const clearCart = document.getElementById('clear-cart')
    clearCart.addEventListener('click', () => {
        selected_products = new Map()
        localStorage.removeItem('selected_products')
        updateSelectedProducts()
    })


});
