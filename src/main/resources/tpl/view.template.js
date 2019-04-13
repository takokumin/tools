﻿/* 画面JS方法 */

"use strict";

var page_list = '#path#/#jspName#.jsp';
var page_edit = '#path#/#jspName#_edit.jsp';

// Init page permission
function initPagePermission() {
    $.gm.ajaxPostJson('gp_page', {pageKey: '#pageKey#'}, false, function (result) {
        if (result['add']) {
            $('#btnAdd').show().bind('click', function () {
                $.curd.add(page_edit);
            });
        } else {
            $('#btnAdd').remove();
        }
        if (result['edit']) {
            $('#btnEdit').show().bind('click', function () {
                $.curd.edit(page_edit, '#pkName#');
            });
        } else {
            $('#btnEdit').remove();
        }
        if (result['remove']) {
            $('#btnRemove').show().bind('click', function () {
                // $.curd.remove('#ifPrefix#_remove', '#pkName#', 'update_on');
                $.curd.batchRemove('#ifPrefix#_batchRemove', '#pkName#', 'update_on');
            });
        } else {
            $('#btnRemove').remove();
        }

    });
}

// Init default table
function initTable() {

    #initFilterContent#

    $.curd.initDefaultTable('#ifPrefix#_list', [
        {checkbox: true}
        #listColumnContent#
        ,{
            field: 'update_on',
            title: $.i18n.prop('txt.common.update.on'),
            halign: 'center',
            align: 'center',
            width: 100
        }
    ]);
}

function initEdit() {

    #initEditCommon#

    if ($('#mode').val() === '2') {
        $.gm.ajaxPostJson('#ifPrefix#_get', $('##moduleName#Form').serializeObject(), true, function (result) {
            if (result.status) {
                var #moduleName# = result['data'];
                $('##pkId#').val(#moduleName#['#pkName#']);
                #initEditContent#
                $('#updateOn').val(#moduleName#['updateOn']);
            } else {
                $.toast.warning($.i18n.prop('msg.no.data.found'));
                loadParent();
            }
        });
    }
    #initAddContent#

    $('##moduleName#Form').validate({
        submitHandler: function (form) {
            $.gm.ajaxFormJson(form, true, function (data) {
                if (data.status) {
                    $.toast.success(data.message);
                    loadParent();
                } else {
                    $.toast.error(data.message);
                }
            });
        }
    });
}

function loadParent() {
    loadMainContent(page_list);
}