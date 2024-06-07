// JavaScript for handling the ordering system

// Event listener for page load to load saved order
window.addEventListener('load', () =>{
    loadSavedOrder();
    fetchMenuItems();
});


async function fetchMenuItems() {
    try {
        const response = await fetch('http://localhost:8080/api/orders/menu', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const menuItems = await response.json();
        displayMenuItems(menuItems);
        addMenuItemListeners();

    } catch (error) {
        console.error('Error fetching menu items:', error);
    }
}

function displayMenuItems(menuItems) {
    const homeMenu = document.getElementById('homeMenu');
    const foodMenu = document.getElementById('menu');
    homeMenu.innerHTML = '';
    foodMenu.innerHTML = '';

    menuItems.forEach(item => {
        const listItem = createMenuItemElement(item);
        homeMenu.appendChild(listItem.cloneNode(true));
        foodMenu.appendChild(listItem);
    });
}

function createMenuItemElement(item) {
    const listItem = document.createElement('li');
    listItem.className = 'list-group-item';
    listItem.dataset.name = item.itemName; 
    listItem.dataset.price = item.price;   
    listItem.dataset.category = item.category;
    listItem.innerHTML = `${item.itemName} - $${item.price}`;
    console.log(item.itemName);
    return listItem;
}

function addMenuItemListeners() {
    document.querySelectorAll('#homeMenu .list-group-item, #menu .list-group-item').forEach(item => {
        item.addEventListener('click', () => {
            const name = item.getAttribute('data-name');
            const price = parseFloat(item.getAttribute('data-price'));
            addToOrder(name, price);
        });
    });
}

let order = [];
let total = 0;

function addToOrder(name, price) {
    let item = order.find(i => i.name === name);
    if (item) {
        item.quantity += 1;
    } else {
        order.push({ name: name, price: price, quantity: 1 });
    }
    updateOrderTable();
    alert(`成功新增 ${name}!`);
}

function removeFromOrder(name) {
    if (confirm(`確定要移除 ${name} 嗎?`)) {
        order = order.filter(item => item.name !== name);
        updateOrderTable();
    }
}

function updateOrderTable() {
    const orderTable = document.getElementById('orderTable');
    orderTable.innerHTML = '';
    total = 0;

    order.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.name}</td>
            <td>${item.quantity}</td>
            <td>${item.price * item.quantity}</td>
            <td><button onclick="removeFromOrder('${item.name}')">Remove</button></td>
        `;
        orderTable.appendChild(row);

        total += item.price * item.quantity;
    });

    document.getElementById('totalPrice').innerText = `Total: $${total}`;
}

function saveOrder() {
    const customerName = document.getElementById('customerName').value;
    const customerPhone = document.getElementById('customerPhone').value;
    const orderDetails = { customerName, customerPhone, order };

    localStorage.setItem('savedOrder', JSON.stringify(orderDetails));
    alert('Order saved!');
}

function loadSavedOrder() {
    const savedOrder = JSON.parse(localStorage.getItem('savedOrder'));
    if (savedOrder) {
        document.getElementById('customerName').value = savedOrder.customerName;
        document.getElementById('customerPhone').value = savedOrder.customerPhone;
        order = savedOrder.order;
        updateOrderTable();
    }
}

async function completeOrder() {
    const customerName = document.getElementById('customerName').value;
    const customerPhone = document.getElementById('customerPhone').value;

    const orderDetails = {
        customerName: customerName,
        customerNumber: customerPhone,
        totalPrice: total,
        status: 'Pending',
        items: []
    };

    // Fetch menuItemIds for each item in the order
    for (const item of order) {
        try {
            const response = await fetch(`http://localhost:8080/api/orders/menu/${item.name}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch menuItemId for item: ${item.name}`);
            }

            const menuItemId = await response.json(); // Assuming the API returns the ID directly as a JSON response
            orderDetails.items.push({
                menuItemId: menuItemId,
                quantity: item.quantity
            });
        } catch (error) {
            console.error('Error fetching menuItemId:', error);
            alert('An error occurred while fetching menu item IDs. Please try again.');
            return; // Exit the function if any fetch fails
        }
    }

    // Submit the order
    try {
        const response = await fetch('http://localhost:8080/api/orders/created', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderDetails)
        });

        if (response.ok) {
            alert('完成訂單!');
            localStorage.removeItem('savedOrder');
            order = [];  // Clear the order
            updateOrderTable();
            document.getElementById('customerName').value = '';
            document.getElementById('customerPhone').value = '';
        } else {
            const errorData = await response.json();
            alert(`Error: ${errorData.message}`);
        }
    } catch (error) {
        console.error('Error completing order:', error);
        alert('An error occurred while completing the order.');
    }
}

function showHome() {
    document.getElementById('homeScreen').style.display = 'block';
    document.getElementById('orderSummary').style.display = 'none';
}

function showOrder() {
    document.getElementById('homeScreen').style.display = 'none';
    document.getElementById('foodMenu').style.display = 'none';
    document.getElementById('orderSummary').style.display = 'block';
}

function showCategory(category) {
    document.getElementById('homeScreen').style.display = 'none';
    document.getElementById('orderSummary').style.display = 'none';
    document.getElementById('foodMenu').style.display = 'block';

    const items = document.querySelectorAll('#menu .list-group-item');
    items.forEach(item => {
        const itemCategory = item.getAttribute('data-category');
        item.style.display = itemCategory === category ? '' : 'none';
    });
}

function searchMenu() {
    const searchBox = document.getElementById('searchBox');
    const searchQuery = searchBox.value.toLowerCase();
    const items = document.querySelectorAll('#homeMenu .list-group-item, #menu .list-group-item');

    items.forEach(item => {
        const itemName = item.dataset.name.toLowerCase();
        item.style.display = itemName.includes(searchQuery) ? 'block' : 'none';
    });
}

// Adding event listeners to menu items
document.querySelectorAll('#homeMenu .list-group-item').forEach(item => {
    item.addEventListener('click', () => {
        const name = item.getAttribute('data-name');
        const price = parseInt(item.getAttribute('data-price'));
        addToOrder(name, price);
    });
});

