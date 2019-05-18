console.log('init');

const token = getMeta("_csrf");
const header = getMeta("_csrf_header");

function getMeta(metaName) {
    const metas = document.getElementsByTagName('meta');

    for (let i = 0; i < metas.length; i++) {
        if (metas[i].getAttribute('name') === metaName) {
            console.log(metas[i].getAttribute('content'));
            return metas[i].getAttribute('content');
        }
    }

    return '';
}

document.addEventListener("DOMContentLoaded", function () {
    let input = document.querySelector('.todo-creator_text-input');
    let list = document.querySelector('.todos-list');
    let buttonClear = document.querySelector('.todos-toolbar_clear-completed');
    let filters = document.querySelectorAll('.todos-toolbar_filters-item');
    let filter = 1;
    let itemsChecked = [];
    let requestRead = new XMLHttpRequest();
    requestRead.onreadystatechange = function () {
        if (requestRead.readyState === XMLHttpRequest.DONE) {
            if (requestRead.status === 200) {
                itemsChecked = JSON.parse(requestRead.responseText);
                redraw();
            }
        }
    };
    requestRead.open("GET", "http://localhost:8080/todo/", true);
    requestRead.setRequestHeader(header,token);
    requestRead.send(null);
    initialization();

    function redraw() {
        var unreadyCounter = 0;
        list.innerHTML = '';
        for (let i = 1; i < 4; i++) {
            document.getElementById("filter" + i).checked = filter === i;
        }
        for (let i = 0; i < itemsChecked.length; i++) {
            if (filter === 1 || filter === 2 && !itemsChecked[i].checked || filter === 3 && itemsChecked[i].checked) {
                addItem(itemsChecked[i].description, itemsChecked[i].checked);
            }
            if (!itemsChecked[i].checked) {
                unreadyCounter++;
            }
        }
        if (unreadyCounter === 0) {
            unreadyCounter = 'none'
        }
        if (unreadyCounter === itemsChecked.length) {
            unreadyCounter = 'All'
        }
        document.querySelector('.todos-toolbar_unready-counter').innerHTML = unreadyCounter + ' items left';
    }


    function initialization() {
        for (let i = 0; i < itemsChecked.length; i++) {
            addItem(itemsChecked[i].description, itemsChecked[i].checked)
        }


        filters[0].addEventListener(
            "click",
            function () {
                filter = 1;
                redraw();
            });
        filters[1].addEventListener(
            "click",
            function () {
                filter = 2;
                redraw();
            });
        filters[2].addEventListener(
            "click",
            function () {
                filter = 3;
                redraw();
            });
        redraw();
    }

    function addItem(description, checked) {    
        var s = '';
        if (checked) {
            s = "checked";
        }
        list.insertAdjacentHTML(
            "beforeend",
            '<div class="todos-list_item">'
            + '<div class="custom-checkbox todos-list_item_ready-marker">'
            + '<input type="checkbox" ' + s + ' class="custom-checkbox_target" aria-label="Mark todo as ready" />'
            + '<div class="custom-checkbox_visual">'
            + '<div class="custom-checkbox_visual_icon"></div>'
            + '</div>'
            + '</div>'
            + '<div class="todos-list_item_text-w">'
            + '<div class="todos-list_item_text" contenteditable="false">' + description + '</div>'
            + '</div>'
            + '<div class = "todos-list_item_remove-action">'
            + '<div class = "todos-list_item_remove-action-fix">'
            + '<buttom class="todos-list_item_remove" aria-label="Delete todo"></buttom>'
            + '</div>'
            + '</div>'
            + '</div>'
        );

        if (checked) {
            let items = list.querySelectorAll('.todos-list_item');
            const textItem = items[items.length - 1].querySelector('.todos-list_item_text');
            textItem.style.color = 'grey';
            textItem.style.textDecoration = 'line-through';
        }


        const removeItems = list.querySelectorAll('.todos-list_item_remove');
        removeItems[removeItems.length - 1].addEventListener(
            "click",
            function (e) {
                e.preventDefault();
                const item = this.closest('.todos-list_item');
                const textItem = item.querySelector('.todos-list_item_text');
                deleteEl(textItem.description);
                removeByText(textItem.description);
                list.removeChild(item);

            }
        );

        const checkBoxItems = list.querySelectorAll('.custom-checkbox_target');
        checkBoxItems[checkBoxItems.length - 1].addEventListener(
            "click",
            function (e) {
                e.preventDefault();
                let item = this.closest('.todos-list_item');
                let textItem = item.querySelector('.todos-list_item_text');
                console.log("Something must be checked " + textItem.description);
                changeChecked(textItem.description);
                redraw();


            }
        );
    }

    input.addEventListener("keydown", function (e) {
        if (e.keyCode === 13) {
            e.preventDefault();
            const text = input.value;
            if (text.length > 0) {
                input.value = "";
                const index = itemsChecked.length;
                itemsChecked[index] = {description: text, checked: false};
                addItem(text, false);
                redraw();

                const formData = new FormData();
                formData.append("description", text);
                const createRequest = new XMLHttpRequest();
                createRequest.onreadystatechange = function () {
                    if (createRequest.readyState === XMLHttpRequest.DONE) {
                        // Everything is good, the response was received.
                        if (createRequest.status === 200) { // Perfect!
                            const responseCreate = JSON.parse(createRequest.responseText);
                            itemsChecked[index].id = responseCreate.id;
                            console.log('good create ' + itemsChecked[index].description)
                        } else {

                        }
                    } else {
                    }
                };
                createRequest.open("POST", "http://localhost:8080/todo");
                createRequest.setRequestHeader(header,token);
                createRequest.send(formData);

            }
        }

    });

    function removeByText(s) {
        for (let i = 0; i < itemsChecked.length; i++) {
            if (itemsChecked[i].description === s) {
                itemsChecked.splice(i, 1);
                return;
            }
        }
    }

    function changeChecked(s) {
        for (let i = 0; i < itemsChecked.length; i++) {
            console.log("checkbox clicked "  + s + ' ' + itemsChecked[i].description);

            if (itemsChecked[i].description === s) {
                itemsChecked[i].checked = !itemsChecked[i].checked;
                const formData = new FormData();
                console.log("checkbox clicked");
                formData.append("description", itemsChecked[i].description);
                formData.append("checked", itemsChecked[i].checked);
                let updateRequest = new XMLHttpRequest();

                updateRequest.onreadystatechange = function () {
                    if (updateRequest.readyState === XMLHttpRequest.DONE) {
                        // Everything is good, the response was received.
                        if (updateRequest.status === 200) { // Perfect!
                            //var responseCreate = JSON.parse(createRequest.responseText);

                            console.log('good update');
                        } else {

                        }
                    } else {
                    }
                };
                updateRequest.open("PUT", "http://localhost:8080/todo/" + itemsChecked[i].id );
                updateRequest.setRequestHeader(header,token);
                updateRequest.send(formData);
                return itemsChecked[i].checked;
            }
        }
        return true;
    }

    function deleteEl(s){
        for (let i = 0; i < itemsChecked.length; i++) {
            if (itemsChecked[i].description === s) {
                let index = itemsChecked[i].id;
                var formData = new FormData();
                formData.append("id", index);
                let deleteRequest = new XMLHttpRequest();

                deleteRequest.onreadystatechange = function () {
                    if (deleteRequest.readyState === XMLHttpRequest.DONE) {
                        // Everything is good, the response was received.
                        if (deleteRequest.status === 200) { // Perfect!
                            //var responseCreate = JSON.parse(createRequest.responseText);

                            console.log('good delete by id ' + index)
                        } else {

                        }
                    } else {
                    }
                };
                deleteRequest.open("DELETE", "http://localhost:8080/todo/");
                deleteRequest.setRequestHeader(header,token);
                deleteRequest.send(formData);
                return itemsChecked[i].checked;
            }
        }
        return true;


    }

    buttonClear.addEventListener(
        "click",
        function (e) {
            e.preventDefault();
            for (let i = 0; i < itemsChecked.length; i++) {
                if (itemsChecked[i].checked) {
                    deleteEl(itemsChecked[i].description);
                    itemsChecked.splice(i, 1);
                    i--;
                }
            }
            redraw();
        }
    )

})
;