window.addEventListener('load', () => {
    fetchMenu();
    fetchOrders();
});

let menuItemsMap = {};
let currentMenuItemId = null;

async function fetchMenu() {
    try {
        const response = await fetch('http://localhost:8080/api/orders/menu', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const menuItems = await response.json();
        displayMenuItems(menuItems);

        // Create a map of menu items for quick lookup
        menuItemsMap = menuItems.reduce((map, item) => {
            map[item.id] = item.itemName;
            return map;
        }, {});
        
    } catch (error) {
        console.error('Error fetching menu items:', error);
    }
}

function displayMenuItems(menuItems) {
    const menuList = document.getElementById('menuList');
    menuList.innerHTML = '';
    
    menuItems.forEach(item => {
        const listItem = createMenuItemElement(item);
        menuList.appendChild(listItem);
    });
}

function createMenuItemElement(item) {
    const listItem = document.createElement('li');
    listItem.className = 'list-group-item';
    listItem.innerHTML = `
        ${item.itemName} - $${item.price}
        <button class="btn btn-danger btn-sm float-end" onclick="deleteMenuItem(${item.id})">刪除</button>
        <button class="btn btn-primary btn-sm float-end" onclick="showUpdateMenuItemModal(${item.id}, '${item.itemName}', ${item.price}, '${item.category}')">更新</button>   
        `;
    return listItem;
}

async function addMenuItem() {
    const itemName = document.getElementById('newMenuItemName').value;
    const price = parseFloat(document.getElementById('newMenuItemPrice').value);
    const category = document.getElementById('newMenuItemCategory').value;

    const menuItem = { itemName, price, category };

    try {
        const response = await fetch('http://localhost:8080/api/orders/menu', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(menuItem)
        });
        const newItem = await response.json();
        fetchMenu();
        document.getElementById('newMenuItemName').value = '';
        document.getElementById('newMenuItemPrice').value = '';
        document.getElementById('newMenuItemCategory').value = '';
        alert(`成功新增 ${itemName}!`)
    } catch (error) {
        console.error('Error adding menu item:', error);
    }
}

function showUpdateMenuItemModal(id, name, price, category) {
    currentMenuItemId = id;
    document.getElementById('updateMenuItemName').value = name;
    document.getElementById('updateMenuItemPrice').value = price;
    document.getElementById('updateMenuItemCategory').value = category;
    new bootstrap.Modal(document.getElementById('updateMenuItemModal')).show();
}

async function saveUpdatedMenuItem() {
    const itemName = document.getElementById('updateMenuItemName').value;
    const price = parseFloat(document.getElementById('updateMenuItemPrice').value);
    const category = document.getElementById('updateMenuItemCategory').value;

    const updatedMenuItem = { itemName, price, category };

    try {
        await fetch(`http://localhost:8080/api/orders/menu/${currentMenuItemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedMenuItem)
        });
        fetchMenu();
        currentMenuItemId = null;
        new bootstrap.Modal(document.getElementById('updateMenuItemModal')).hide();
        document.getElementById('updateMenuItemModal').hide();
    } catch (error) {
        console.error('Error updating menu item:', error);
    }
}   

async function deleteMenuItem(id) {
    if (confirm(`確定要移除嗎?`)) {
        try {
        await fetch(`http://localhost:8080/api/orders/menu/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        fetchMenu();
    } catch (error) {
        console.error('Error deleting menu item:', error);
    }
    }
}

async function fetchOrders() {
    try {
        const response = await fetch('http://localhost:8080/api/orders', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const orders = await response.json();
        displayOrders(orders);
    } catch (error) {
        console.error('Error fetching orders:', error);
    }
}

function displayOrders(orders) {
    const orderList = document.getElementById('orderList');
    orderList.innerHTML = '';

    orders.forEach(order => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        
        listItem.innerHTML = `
            <div>
                <h5>訂單 ${order.id}</h5>
                <p>顧客: ${order.customerName}</p>
                <p>電話: ${order.customerNumber}</p>
                <p>總金額: $${order.totalPrice}</p>
                <p>狀態: <span class="badge badge-pill">${order.status}</span></p>
                <p>品項:</p>
                <ul>${order.items.map(item => `<li>${menuItemsMap[item.menuItemId]} x${item.quantity}</li>`).join('')}</ul>
                <button class="btn btn-danger btn-sm float-end" onclick="deleteOrder(${order.id})">刪除訂單</button>
                <hr style="border-width:8px;">
            </div>
        `;
        orderList.appendChild(listItem);
    });
}
async function deleteOrder(id) {
    if (confirm(`確定要移除訂單${id}嗎?`)) {
        try {
        await fetch(`http://localhost:8080/api/orders/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        fetchOrders();
    } catch (error) {
        console.error('Error deleting order:', error);
    }
    }
}
        