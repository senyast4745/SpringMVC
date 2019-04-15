var Todos = {
    el: {
        item: '.todos-list_item',
        notDeleted: ':not(.deleted)',
        deleted: '.deleted',
        visible: ':visible'
    },

    init: function () {
        var t = this;
        this.updateCount();

        // Add new element
        $('.todo-creator').on('submit', function (e) {
            e.preventDefault();
            t.itemFactory($('.todo-creator_text-input').val());
            t.updateCount();

            var formData = new FormData();
            formData.append("description", e.item);

// HTML file input
            httpRequest.open("POST", "/create", true);
            httpRequest.onload = function (ev) {
                var arrayBuffer = ev.response; // not responseText
                if (arrayBuffer) {
                    alert("sending")
                }
            };
            httpRequest.send(formData);

        });

        // Remove element
        $(document).on('click', '.todos-list_item_remove', function () {
            $(this).closest(t.el.item).addClass('deleted').hide();
            t.updateCount();
        });

        // Remove all
        $('.todos-toolbar_clear-completed').on('click', function () {
            $(t.el.item).remove();
            t.updateCount();
        });

        // Filter all
        $('.filters-item_selected').on('click', function () {
            t.hideItems();
            $(t.el.item + t.el.notDeleted).show();
            t.updateCount();
        });

        // Filter active
        $('.filters-item.active').on('click', function () {
            t.hideItems();
            $('.custom-checkbox_target:checked').closest(t.el.item + t.el.notDeleted).show();
            t.updateCount();
        });

        // Filter completed
        $('.filters-item.completed').on('click', function () {
            t.hideItems();
            $(t.el.item + t.el.deleted).show();
            t.updateCount();
        });
    },

    hideItems: function () {
        $(this.el.item).hide();
    },

    updateCount: function () {
        $('.todos-toolbar_unready-counter .digit').html($(this.el.item + this.el.visible).length);
    },

    itemFactory: function (value) {
        var html =
            '<div class="todos-list_item">' +
            '<div class="custom-checkbox todos-list_item_ready-marker">' +
            '<input type="checkbox" class="custom-checkbox_target" aria-label="Mark todo as ready">' +
            '<div class="custom-checkbox_visual">' +
            '<div class="custom-checkbox_visual_icon"></div>' +
            '</div>' +
            '</div>' +
            '<div class="todos-list_item_remove-action">' +
            '<div class="todos-list_item_remove-action-fix">' +
            '<buttom class="todos-list_item_remove" aria-label="Delete todo"></buttom>' +
            '</div>' +
            '</div>' +
            '<div class="todos-list_item_text-w">' +
            '<div class="todos-list_item_text">' + value + '</div>' +
            '</div>' +
            '</div>';

        $('.todos-list').append(html);
    }
};

$(document).ready(function () {
    Todos.init();
});