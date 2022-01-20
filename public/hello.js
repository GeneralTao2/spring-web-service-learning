/*$(document).ready(function() {
    $.ajax({
        url: "/employees"
    }).then(function(data) {
       $('.greeting-id').append(data._embedded.employeeList[0].id);
       $('.greeting-content').append(data._embedded.employeeList[0].firstName);
    });
});*/

/*var httpRequest = new XMLHttpRequest();
httpRequest.onreadystatechange = function(){
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
        if (httpRequest.status === 200) {
            alert(httpRequest.responseText)
        } else {
            // There was a problem with the request.
            // For example, the response may have a 404 (Not Found)
            // or 500 (Internal Server Error) response code.
        }
        
    } else {
        // Not ready yet.
    }
    
};
httpRequest.open('GET', '/employees', true);
httpRequest.send();*/
var myModal;
var editOrderModal;
var addOrderModal;
document.addEventListener("DOMContentLoaded", () => {
    myModal = new bootstrap.Modal(document.getElementById("exampleModal"));
    editOrderModal = new bootstrap.Modal(document.getElementById("edit-order-modal"));
    addOrderModal = new bootstrap.Modal(document.getElementById("add-order-modal"));
    fetch('/employees')
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            data._embedded.employeeList.forEach(element => {
                insertColToEmplyeeList(wrapCardToColumn(insertEmplyeeToCard(element)));
            });
        })

    document.getElementById('btn-add-employee').addEventListener('click', btnAddEmployeeEventListener);

    document.getElementById('btn-edit-order').addEventListener('click', btnEditOrderEventListener);

    document.getElementById('add-new-card').addEventListener('click', function () {
        document.getElementById('btn-add-employee').removeEventListener('click', btnEditEmployeeEventListener);
        document.getElementById('btn-add-employee').addEventListener('click', btnAddEmployeeEventListener);
        document.getElementById('btn-add-employee').innerText = 'Add';
    });

    document.getElementById('add-new-card-add').addEventListener('click', btnAddOrderEventListener);

    document.getElementById('employee-list').addEventListener('click', (event) => {
        if (event.target.className === 'delete employee-card-btn') {
            myModal.currentCard = event.target.parentElement;
            var id = event.target.parentElement.querySelector('.id-holder').innerText;
            fetch(`/employees/${id}`, {
                method: 'DELETE'
            })
                .then(() => {

                }, (error) => console.log(error))
                .then(
                    () => {
                        event.target.parentElement.remove()
                    },
                    (error) => {
                        console.log(error)
                    }
                )
        }
    });

    document.getElementById('employee-list').addEventListener('click', (event) => {
        if (event.target.className === 'edit employee-card-btn') {
            myModal.currentCard = event.target.parentElement;
            var id = event.target.parentElement.querySelector('.id-holder').innerText;
            fetch(`/employees/${id}`)
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    document.getElementById('input-firstname').value = data.firstName;
                    document.getElementById('input-lastname').value = data.lastName;
                    document.getElementById('input-role').value = data.role;
                })
                .then(() => {
                    console.log('edit employee modal window');
                    document.getElementById('btn-add-employee').removeEventListener('click', btnAddEmployeeEventListener);
                    document.getElementById('btn-add-employee').addEventListener('click', btnEditEmployeeEventListener);
                    document.getElementById('btn-add-employee').innerText = 'Edit';
                    myModal.show();
                })
        }
    });

    document.getElementById('employee-list').addEventListener('click', (event) => {
        if (event.target.className === 'delete order-btn') {
            var card = event.target.parentElement.parentElement.parentElement.parentElement;
            var cardId = getIdFromCard(card);
            var index = event.target.parentElement.value;
            fetch(`/employees/${cardId}/orders/${index}`, {
                method: 'DELETE'
            })
                .then(() => {

                }, (error) => console.log(error))
                .then(
                    () => {
                        event.target.parentElement.remove()
                    },
                    (error) => {
                        console.log(error)
                    }
                )
        }
    });

    document.getElementById('employee-list').addEventListener('click', (event) => {
        if (event.target.className === 'edit order-btn') {
            editOrderModal.currentOrder = event.target.parentElement;
            var id = event.target.parentElement.querySelector('.id-holder').innerText;
            fetch(`/orders/${id}`)
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    document.getElementById('edit-order-input-description').value = data.description;

                    checkRadioButton('edit-order-radio', data.status);
                })
                .then(() => {
                    editOrderModal.show();
                })
        }
    });

    document.getElementById('employee-list').addEventListener('click', (event) => {
        if (event.target.className === 'btn btn-dark add-order-btn') {
            document.getElementById('add-new-order-input-decription').disabled = false;
            document.getElementById('add-order-radio-in-progress').disabled = false;
            document.getElementById('add-order-radio-completed').disabled = false;
            document.getElementById('add-order-radio-cancelled').disabled = false;
            document.getElementById('select-order').disabled = true;
            if( document.querySelector('input[name="add-order-mode"]:checked'))
                document.querySelector('input[name="add-order-mode"]:checked').checked = false;
            document.getElementById('add-order-mode-new').checked = true;
            document.getElementById('add-new-order-input-decription').value = '';
            document.querySelector('input[name="add-order-radio"]:checked').checked = false;
            document.getElementById('add-order-radio-in-progress').checked = true;

            var card = event.target.parentElement.parentElement.parentElement.parentElement;
            addOrderModal.currentCard = card;
            fetch('/orders', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(function (response) {
                    return response.json();
                }
                )
                .then(function (data) {
                    var selectOrder = document.getElementById('select-order');
                    selectOrder.innerHTML = '<option selected>Choose order...</option>';
                    data._embedded.orderList.forEach(element => {
                        var option = document.createElement('option');
                        option.value = element.id;
                        option.innerText = element.description;
                        selectOrder.appendChild(option);
                    });
                    addOrderModal.show();
                })
        }
    });

    document.getElementById('add-order-mode-new').addEventListener('click', function () {
        document.getElementById('add-new-order-input-decription').disabled = false;
        document.getElementById('add-order-radio-in-progress').disabled = false;
        document.getElementById('add-order-radio-completed').disabled = false;
        document.getElementById('add-order-radio-cancelled').disabled = false;
        document.getElementById('select-order').disabled = true;
    });

    document.getElementById('add-order-mode-existing').addEventListener('click', function () {
        document.getElementById('add-new-order-input-decription').disabled = true;
        document.getElementById('add-order-radio-in-progress').disabled = true;
        document.getElementById('add-order-radio-completed').disabled = true;
        document.getElementById('add-order-radio-cancelled').disabled = true;
        document.getElementById('select-order').disabled = false;
    });

});

function checkRadioButton(radioGroupName, caseValue) {
    var radios = document.getElementsByName(radioGroupName);
    for (var i = 0, length = radios.length; i < length; i++) {
        radios[i].checked = false;
    }

    switch (caseValue) {
        case 'IN_PROGRESS':
            radios[0].checked = true;
            break;
        case 'COMPLETED':
            radios[1].checked = true;
            break;
        case 'CANCELED':
            radios[2].checked = true;
            break;
    }
}

function btnAddEmployeeEventListener() {
    var firstName = document.getElementById('input-firstname').value;
    var lastName = document.getElementById('input-lastname').value;
    var role = document.getElementById('input-role').value;
    var employee = {
        firstName: firstName,
        lastName: lastName,
        role: role
    };
    fetch('/employees', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    })
        .then((response) => response.json(), (error) => console.log(error))
        .then((data) => insertColToEmplyeeList(wrapCardToColumn(insertEmplyeeToCard(data))),
            (error) => console.log(error)
        )
}

function btnEditOrderEventListener() {
    var id = editOrderModal.currentOrder.querySelector('.id-holder').innerText;
    var description = document.getElementById('edit-order-input-description').value;
    var status = document.querySelector('input[name="edit-order-radio"]:checked').value;
    var order = {
        description: description,
        status: status
    };
    console.log(order);
    fetch(`/orders/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(order)
    })
        .then((response) => response.json(), (error) => console.log(error))
        .then((data) => {
            editOrderModal.currentOrder.querySelector('.order-description').innerText = data.description;
            editOrderModal.currentOrder.querySelector('.order-status').innerText = data.status;
            editOrderModal.hide();
        },
            (error) => console.log(error)
        )
}

function btnEditEmployeeEventListener(event) {
    var firstName = document.getElementById('input-firstname').value;
    var lastName = document.getElementById('input-lastname').value;
    var role = document.getElementById('input-role').value;
    var id = getIdFromCard(myModal.currentCard);
    var employee = {
        name: firstName + ' ' + lastName,
        role: role,
    };
    fetch(`/employees/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(employee)
    })
        .then((response) => response.json(), (error) => console.log(error))
        .then((data) => {
            editEmployeeInCard(myModal.currentCard, employee);
            myModal.hide();
        },
            (error) => console.log(error)
        )
}

function isNotFirstCard(card) {
    return document.querySelector('.card') !== card;
}

function getIdFromCard(card) {
    return card.querySelector('.id-holder').innerText;
}

function findCardById(id) {
    var cards = document.getElementsByClassName('card');
    for (var i = 0; i < cards.length; i++) {
        if (isNotFirstCard(cards[i]) && (getIdFromCard(cards[i]) === id)) {
            return cards[i];
        }
    }
}

function insertColToEmplyeeList(column) {
    document.getElementById('employee-list').appendChild(column);
}

function editEmployeeInCard(card, employee) {
    card.querySelector('.name-holder').innerText = employee.name;
    card.querySelector('.role-holder').innerText = employee.role;
}


function insertEmplyeeToCard(employee) {
    var card = document.createElement('div');
    card.className = 'card';
    card.style.width = '18rem';
    card.innerHTML = `
        <p class="delete employee-card-btn">✖️</p>
        <p class="edit employee-card-btn">🖊️</p>
        <div class="card-body">
            <h5 class="card-title name-holder">${employee.name}</h5>
            <h6 class="card-subtitle mb-2 text-muted role-holder">${employee.role}</h6>
            <p class="card-text id-holder">${employee.id}</p>
            <ul class="list-group">
                <a href="#" class="btn btn-dark add-order-btn">+</a>
            </ul>

            <a href="${employee._links.self.href}" class="card-link">self</a>
            <a href="${employee._links.employees.href}" class="card-link">employees</a>
        </div>
    `;
    console.log(employee);
    if(employee.orders) {
        for (var i = 0; i < employee.orders.length; i++) {
            insertOrderToCard(card, employee.orders[i], i);
        }
    }
    return card;
}

function insertOrderToCard(card, order, i) {
    var orderList = card.querySelector('.list-group');
    var orderItem = document.createElement('li');
    orderItem.className = 'list-group-item order';
    orderItem.value = i;
    orderItem.innerHTML = `
        <p class="delete order-btn">✖️</p>
        <p class="edit order-btn">🖊️</p>
        <p class="order-text order-description">${order.description}</p>
        <p class="order-text order-status">${order.status}</p>
        <p class="order-text id-holder">${order.id}</p>
    `;
    orderList.appendChild(orderItem);
}

function wrapCardToColumn(card) {
    var column = document.createElement('div');
    column.className = 'col';
    column.appendChild(card);
    return column;
}

function btnAddOrderEventListener(event) {
    addOrderModal.hide();
    var id = getIdFromCard(addOrderModal.currentCard);
    var addOrderModeRadio = document.querySelector('input[name="add-order-mode"]:checked').value;
    if (addOrderModeRadio === 'new') {
        var description = document.getElementById('add-new-order-input-decription').value;
        var status = document.querySelector('input[name="add-order-radio"]:checked').value;
        var order = {
            description: description,
            status: status
        };
        fetch(`/employees/${id}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order)
        })
            .then((response) => response.json(), (error) => console.log(error))
            .then((data) => {
                insertOrderToCard(addOrderModal.currentCard, data.orders.at(-1), addOrderModal.currentCard.querySelector('.list-group').childElementCount);
                addOrderModal.hide();
            },
                (error) => console.log(error)
            )
    } else {
        var orderId = document.getElementById('select-order').value;
        fetch(`/employees/${id}/orders/${orderId}`, {
            method: 'GET'
        })
            .then((response) => response.json(), (error) => console.log(error))
            .then((data) => {
                console.log(data);
                var order = Array.from(document.querySelectorAll('.order-text.id-holder'))
                    .find(el => el.textContent === orderId);
                console.log(order);
                if(order) {
                    var orderList = order.parentElement.parentElement;
                    orderList.removeChild(order.parentElement);
                }
                insertOrderToCard(addOrderModal.currentCard, data.orders.at(-1), addOrderModal.currentCard.querySelector('.list-group').childElementCount);
                
                addOrderModal.hide();
            },
                (error) => console.log(error)
            )
    }
}


