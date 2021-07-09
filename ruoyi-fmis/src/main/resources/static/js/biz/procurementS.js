/**
 * 采购池
 * @type {string}
 * 合同
 *      产品信息
 *          XXX
 *      备选产品信息
 *          XXX
 *      执行器信息
 *          XXX
 *      备选执行器
 */
var prefixPool = ctx + "fmis/procurementpool";

var procurementId = $("#dataId").val();

$(function() {
    // window.opener.reload();
    var options = {
        url: prefixPool + "/list",
        modalName: "采购池",
        uniqueId: "dataId",
        detailView: true,
        cache: true,
        async: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildLevelTable(index, row, $detail);
        },
        columns: [{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {
            checkbox: true
            },
            {
                field : 'dataId',
                title : 'ID',
                visible: false
            },
            {
                field : 'string1',
                title : '合同编号'
            },
            {
                field : 'string30',
                title : '处理状态',
                formatter: function(value, row, index) {
                    var actions = [];
                    var v = row["string30"];
                    var showText = "未处理";
                    if (value == "1") {
                        showText = "部分处理";
                    } else if (value == "2") {
                        showText = "处理完成";
                    }
                    actions.push($.common.sprintf("<span class='default'>%s</span>", showText));
                    return actions;
                }
            },
            {
                field : 'datetime1',
                title : '签订日期'
            },
            {
                field : 'string3',
                title : '供方',
                formatter: function(value, row, index) {
                    return $.table.selectDictLabel(supplierTypeData, value);
                }
            },
            {
                field : 'deptName',
                title : '归属部门',
            },
            {
                field : 'string6',
                title : '发货时间'
            },
            {
                field : 'string25',
                title : '特殊要求'
            }
        ]
    };
    // $.table.clear;
    $.table.init(options);
});
var overAllIds = new Array();  //全局数组
var numberMap = new Map();
var priceMap = new Map();
var supplierMap = new Map();
//为了带过去销售合同中的供方以及归属单位
var haoliMap = new Map();
var guishudanwei = new Map();
var string3_parent = "";
var deptName_parent = "";
/**
 * 展开父子表
 */
function initExpandRow() {
    $("#loadingModalFmis").modal('show');
    setTimeout(function () {
        var repeartMap = new Map();

        console.log("loadingModalFmis show..." + overAllIds.length,overAllIds)
        var initOver = false;
        for (var i = 0; i < overAllIds.length; i++) {
            var pathId = overAllIds[i];
            console.log(pathId)
            var pathIds = pathId.split("_");
            var type = pathIds[0];//类型
            var childId = pathIds[1];//合同子数据ID
            var busDataId = pathIds[2];//例如 产品ID
            var parentDataId = pathIds[3];//合同ID
            var levelValue = pathIds[4];//A,B,C
            var repeartKey = type + "_" + parentDataId + "_" + levelValue;
            if (repeartMap.get(repeartKey) != null) {
                continue;
            }
            console.log("parentDataIdsss:" + parentDataId)
            console.log("parentDataId11:" + $("#bootstrap-table").bootstrapTable('getRowByUniqueId', parentDataId))
            repeartMap.set(repeartKey,i);
            $("#bootstrap-table").bootstrapTable('expandRow', $("#bootstrap-table").bootstrapTable('getRowByUniqueId', parentDataId).rowId);
            var levelUniqueId = "";
            if (levelValue == "A") {
                levelUniqueId = type + "1";
            } else if (levelValue == "B") {
                levelUniqueId = type + "2";
            } else if (levelValue == "C") {
                levelUniqueId = type + "3";
            }
            var initChildLevelTableId = "initChildLevelTable_" + parentDataId;
            expandChildLevelTablePromise(initChildLevelTableId,levelUniqueId).then(function () {
                //console.log("initExpandRow i=" + i);
                $("#loadingModalFmis").modal('hide');
            })

            //展开合同类别
            var bizEditFlag = $("#bizEditFlag").val();
            if (bizEditFlag == 2) {

            }
            if (bizEditFlag == 0) {
                var paramterSupplierId = parent.$('#paramterSupplierId').val();
                var totalPrice = parent.$('#totalPrice').val();

                $("#price1").val(totalPrice);
                //增加的时候 把供应商
                $("#string6").find("option[value='" + paramterSupplierId + "']").attr("selected",true);

            } else {

            }
        }
        if (bizEditFlag == 1) {
            $("#price1").val($("#price1Copy").val());
        }
    }, 3000);


}

function isDouble (v) {
    var reg = /^[0-9,.]*$/ //^[-\+]?\d+(\.\d+)?$/;
    if (reg.test(v)) {
        return true;
    } else {
        return false;
    }
}

function expandChildLevelTablePromise(initChildLevelTableId,levelUniqueId) {
    var promise;
    console.log("initChildLevelTableId=" + initChildLevelTableId);
    promise = new Promise(function(resolve, reject) {
        $("#" + initChildLevelTableId).on("load-success.bs.table",{unid: levelUniqueId},(function(e){
            console.log("levelUniqueId12=" + levelUniqueId);
            $("#" + initChildLevelTableId).bootstrapTable('expandRow', $("#" + initChildLevelTableId).bootstrapTable('getRowByUniqueId', e.data.unid).rowId);
            console.log("expandChildLevelTablePromise... ...");
        }));
        resolve();
    });
    return promise;
}

function cellStyle (value, row, index) {
    return {
        width: 100,
        css: {
            'background':'red'
        }
    }
}

initChildLevelTable = function(index, row, $detail) {

    var initChildLevelTableId = "initChildLevelTable_" + row.dataId;
    console.log("initChildLevelTableId1=" + initChildLevelTableId);
    var cur_table = $detail.html('<table style="table-layout:fixed" id=' + initChildLevelTableId + ' data-cache="true"></table>').find('table');
    var dataId = row["dataId"];

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelS",
        method: 'post',
        detailView: true,
        cache: true,
        rowStyle: function(row, index) {
            return {classes:'danger'};
        },
        async: false,
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "levelType",
        queryParams : {
            "dataStatus": $("#dataStatus").val(),
            "dataId": dataId,
            "bizEditFlag":$("#bizEditFlag").val(),
            "supplierId":$("#string6").val(),
            "procurementId": procurementId

        },
        onExpandRow : function(index, row, $detail) {
            initChildLevelListTable(index, row, $detail);
        },columns: [
            {field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'levelTypeName',title : '类别'},
            {field : 'levelType',title : 'type',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'level',title : 'level',visible: false}
        ]
    });
};

var numJsonValue = parent.$('#numJsonValue').val();
if ($.common.isEmpty(numJsonValue)) {
    numJsonValue = $("#numJsonValue").val();
}
if (!$.common.isEmpty(numJsonValue)) {
    var numJson = JSON.parse(numJsonValue);
    for (var i = 0; i < numJson.length; i++) {
        var numJsonData = numJson[i];
        var id = numJsonData.id;
        var value = numJsonData.num;
        overAllIds.push(id);
        if (value != "-1") {
            numberMap.set(id,value);
        }
    }
    calculatePrice();
}


//保存数量
function onEditableSave (field, row, oldValue, $el) {
    var columnName1 = "";
    var columnName2 = "";
    var tableName = "";
    console.log("onEditableSave1 ... ... ");
    if (field == "productNum") {
        columnName1 = "productNum";
        columnName2 = "productProcurementPrice";
        tableName = "initChildProductTable_" + row.dataId;
        var tempNum = row.productNum;
        numberMap.set("1_" + row.childId + "_" + row.productId + "_" + row.dataId + "_" + row.levelValue,row.productNum);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        //row.productNum = tempNum;
        console.log("row.productNum=" + row.productNum + " tempNum=" + tempNum);
        priceMap.set("1_" + row.childId + "_" + row.productId + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "actuatorNum") {
        columnName1 = "actuatorNum";
        columnName2 = "actuatorString6";
        tableName = "initChildActuatorTable_" + row.dataId;
        numberMap.set("2_" + row.childId + "_" + row.actuatorId + "_" + row.dataId + "_" + row.levelValue,row.actuatorNum);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("2_" + row.childId + "_" + row.actuatorId + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "productRef1Num") {
        columnName1 = "productRef1Num";
        columnName2 = "ref1String2";
        tableName = "initChildRef1Table_" + row.dataId;
        numberMap.set("3_" + row.childId + "_" + row.productRef1Id + "_" + row.dataId + "_" + row.levelValue,row.productRef1Num);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("3_" + row.childId + "_" + row.productRef1Id + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "productRef2Num") {
        columnName1 = "productRef2Num";
        columnName2 = "ref1String2";
        tableName = "initChildRef2Table_" + row.dataId;
        numberMap.set("4_" + row.childId + "_" + row.productRef2Id + "_" + row.dataId + "_" + row.levelValue,row.productRef2Num);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("4_" + row.childId + "_" + row.productRef2Id + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "pattachmentCount") {
        columnName1 = "pattachmentCount";
        columnName2 = "procurementPrice";
        tableName = "initChildPATable_" + row.dataId;
        numberMap.set("5_" + row.childId + "_" + row.pattachmentId + "_" + row.dataId + "_" + row.levelValue,row.pattachmentCount);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("5_" + row.childId + "_" + row.pattachmentId + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "pattachment1Count") {
        columnName1 = "pattachment1Count";
        columnName2 = "procurementPrice";
        tableName = "initChildPA1Table_" + row.dataId;
        numberMap.set("6_" + row.childId + "_" + row.pattachment1Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment1Count);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("6_" + row.childId + "_" + row.pattachment1Id + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "pattachment2Count") {
        columnName1 = "pattachment2Count";
        columnName2 = "procurementPrice";
        tableName = "initChildPA2Table_" + row.dataId;
        numberMap.set("7_" + row.childId + "_" + row.pattachment2Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment2Count);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);

        priceMap.set("7_" + row.childId + "_" + row.pattachment2Id + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "pattachment3Count") {
        columnName1 = "pattachment3Count";
        columnName2 = "procurementPrice";
        tableName = "initChildPA3Table_" + row.dataId;
        numberMap.set("8_" + row.childId + "_" + row.pattachment3Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment3Count);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);

        priceMap.set("8_" + row.childId + "_" + row.pattachment3Id + "_" + row.dataId + "_" + row.levelValue,price1);
    } else if (field == "pattachment4Count") {
        columnName1 = "pattachment4Count";
        columnName1 = "pattachment4Count";
        columnName2 = "procurementPrice";
        tableName = "initChildPA4Table_" + row.dataId;
        numberMap.set("9_" + row.childId + "_" + row.pattachment4Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment4Count);
        var price1 = setRowTotalPrice(columnName1,columnName2,tableName,row);
        priceMap.set("9_" + row.childId + "_" + row.pattachment4Id + "_" + row.dataId + "_" + row.levelValue,price1);
    }
    //console.log("no calculatePrice");
    calculatePrice();
}

function calculatePrice () {
    var price1 = 0;
    priceMap.forEach(function (value, key, map) {
        price1 = FloatAdd(price1,value);
    });
    $("#price1").val(price1);
}
function FloatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}

function setRowTotalPrice(columnName1,columnName2,tableName,row) {
    var productNum = row[columnName1];
    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
    var procurementPrice = row[columnName2];
    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
    var total = parseFloat(productNum * procurementPrice).toFixed(2);
    var rowId = parseInt(row["rowId"]);

    var rowCheckStatus = row[0];
    if (rowCheckStatus) {
        //row.checked = false;
        //console.log("check no... ...");
    }
    console.log("rowCheckStatus1=" + rowCheckStatus);
    var updateObj = {
        index: rowId,
        field: "totalPrice",
        value: total
    };
    row["totalPrice"] = total;
    //console.log("updateRow2=" + JSON.stringify(row));
    //$("#" + tableName).bootstrapTable('updateRow', {index: rowId, row: row});
    $("#" + tableName).bootstrapTable("updateCell", updateObj);
    row[columnName1] = productNum;
//updateRow

    /*var updateObj1 = {
        index: rowId,
        field: columnName1,
        value: productNum
    };
    console.log("updateCell num=" + JSON.stringify(updateObj1));
    $("#" + tableName).bootstrapTable("updateCell", updateObj1);*/
    if (rowCheckStatus) {
        //row.checkbox = false;
    }
    return total;
}

function showNum(type,row) {
    console.log("showNum ... ... ");
    if (type == 1) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.productId + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            console.log("showNum1 num=" + num);
            row.productNum = num;
        }
    } else if (type == 2) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.actuatorId + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.actuatorNum = num;
        }
    } else if (type == 3) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.productRef1Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.productRef1Num = num;
        }
    } else if (type == 4) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.productRef2Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.productRef2Num = num;
        }
    } else if (type == 5) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.pattachmentId + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.pattachmentCount = num;
        }
    } else if (type == 6) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.pattachment1Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.pattachment1Count = num;
        }
    } else if (type == 7) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.pattachment2Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.pattachment2Count = num;
        }
    } else if (type == 8) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.pattachment3Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.pattachment3Count = num;
        }
    } else if (type == 9) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.pattachment4Id + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
            row.pattachment4Count = num;
        }
    }

}
function formaterStatus (value, row, index) {
    var status = row.dataStatus;
    if (status == "1") {return "<font color=red>已处理</font>"} else {return "<font color=green>未处理</font>"}

}
//禁止选择逻辑
function disableCheckbox (row) {
    var disableValue = "1";
    if ("1" == $("#bizEditFlag").val() && row.procurementId == $("#dataId").val()) {
        return disableValue;
    }
    if (row.dataStatus == "1" || $("#bizEditFlag").val() == "2") {
        disableValue = "-1";
    }
    return disableValue;
}

initChildProductTable = function(index, row, $detail) {
    //var tableHtml = '<div class="col-sm-12 select-table table-striped"><table style="table-layout:fixed" id="initChildProductTable_' + row.dataId + '"></table></div>';

    var tableHtml = '<div class="table-responsive"><table class="table text-nowrap"  id="initChildProductTable_' + row.dataId + '"></table></div>';


    var cur_table = $detail.html(tableHtml).find('table');
    var pSessionId = parent.$('#formId input[id=pSessionId]').val();
    console.log("parent formId pSessionId=" + pSessionId);
    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelProduct",
        method: 'post',
        async: false,
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "productId",
        cache: true,
        onEditableSave: onEditableSave,
        onClickCell: function(field, value, row, $element) {
            onClickProductCell(field, value, row, $element,row.dataId);
        },
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "bizEditFlag":$("#bizEditFlag").val(),
            "supplierId":$("#string6").val(),
            "procurementId": procurementId,
            "pSessionId": pSessionId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("1_" + row.childId + "_" + row.productId + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(1,row);
                    checkedValue = true;
                } else {

                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }

            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'productId',title : '产品ID1',visible: false},
            {field : 'newProductId',title : 'new产品ID',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},

            {field : 'contractNo',title : '合同编号',visible: true},

            {field : 'procurementId',title : 'childId',visible: false},

            {field : 'productName',title : '产品名称',editable: false},
            {field : 'string18',title : '标准产品',editable: false,formatter: function(value, row, index) {
                    var actions = '';
                    var isStand = row.string18;
                    if (isStand == "yes") {
                        actions = '<font color=green>是</font>';
                    } else {
                        actions = '非';
                    }
                    return actions;
                }},
            {field : 'model',title : '型号',editable: false},
            {field : 'series',title : '系列',editable: false},
            {field : 'productNum',title : '数量',editable: {type: 'text',validate: function(v,r){ return numberValidate(v)}}},
            {field : 'productProcurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["productNum"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["productProcurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},

            {field : 'productStatus',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'specifications',title : '规格',editable: false},
            {field : 'nominalPressure',title : '压力',editable: false},
            {field : 'valvebodyMaterial',title : '阀体',editable: false},
            {field : 'valveElement',title : '阀芯',editable: false},
            {field : 'sealingMaterial',title : '密封材质',editable: false},
            {field : 'driveForm',title : '驱动形式',editable: false},
            {field : 'connectionType',title : '连接方式',editable: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            // {field : 'contractSpecial',title : '特殊要求',editable: false}


        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,1,row["dataId"]);
    });
};

function onClickProductCell (field, value, row, $element,dataId) {
    var bizEditFlag = $("#bizEditFlag").val();
    if (field == "productName" && bizEditFlag == -1) {
        var idx = $element.parent().data('index');
        var tableId = "initChildProductTable_" + dataId;
        console.log("-1=" + tableId);
        var productId = row.productId;
        var widthNum = $("#formId").width() - 50;
        var heigthNum = $("#formId").height() + 450;
        var url = prefixPool + "/selectProduct?productId=" + productId;
        console.log("widthNum=" + widthNum + "---" + this.innerWidth);
        $.modal.open("关联产品配件法兰", url,widthNum, heigthNum,function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.submitHandler(index, layero);
            //ref1JsonParamter
            var productsJsonParamter = $("#productsJsonParamter").val();
            setTableValueById(tableId,productsJsonParamter,productId,idx);
        });

    }

}
//根据表ID修改某一列的值
function setTableValueById (tableId,jsonValue,id,idx) {
    var jsonObj = JSON.parse(jsonValue);
    for (key in jsonObj) {
        var updateObj = {
            index : idx,
            field : key,
            value : jsonObj[key]
        };
        $("#" + tableId).bootstrapTable("updateCell",updateObj);
        console.log("updateCell=" + JSON.stringify(updateObj));
    }
}

function examine(type,datas,typeIndex,id){
    console.log("type:" + type)
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            var dataId = "";
            var guishu = "";
            var num = "0";
            var price = 0;
            var columnName1 = "";
            var columnName2 = "";
            var tableName = "";
            console.log("typeIndex:" + typeIndex)
            console.log("v:" + v)
            if (typeIndex == 1) {
                dataId = v.productId;
                num = v.productNum;
                price = v.productProcurementPrice;
                columnName1 = "productNum";
                columnName2 = "productProcurementPrice";
                tableName = "initChildProductTable_" + v.dataId;
            } else if (typeIndex == 2) {
                dataId = v.actuatorId;
                num = v.actuatorNum;
                price = v.actuatorString6;
                columnName1 = "actuatorNum";
                columnName2 = "actuatorString6";
                tableName = "initChildActuatorTable_" + v.dataId;
            } else if (typeIndex == 3) {
                dataId = v.productRef1Id;
                num = v.productRef1Num;
                price = v.ref1String2;
                columnName1 = "productRef1Num";
                columnName2 = "ref1String2";
                tableName = "initChildRef1Table_" + v.dataId;
            } else if (typeIndex == 4) {
                dataId = v.productRef2Id;
                num = v.productRef2Num;
                price = v.ref1String2;
                columnName1 = "productRef2Num";
                columnName2 = "ref1String2";
                tableName = "initChildRef2Table_" + v.dataId;
            } else if (typeIndex == 5){
                dataId = v.pattachmentId;
                num = v.pattachmentCount;
                price = v.procurementPrice;
                columnName1 = "pattachmentCount";
                columnName2 = "procurementPrice";
                tableName = "initChildPATable_" + v.dataId;
            } else if (typeIndex == 6){
                dataId = v.pattachment1Id;
                num = v.pattachment1Count;
                price = v.procurementPrice;
                columnName1 = "pattachment1Count";
                columnName2 = "procurementPrice";
                tableName = "initChildPA1Table_" + v.dataId;
            } else if (typeIndex == 7){
                dataId = v.pattachment2Id;
                num = v.pattachment2Count;
                price = v.procurementPrice;
                columnName1 = "pattachment2Count";
                columnName2 = "procurementPrice";
                tableName = "initChildPA2Table_" + v.dataId;
            } else if (typeIndex == 8){
                dataId = v.pattachment3Id;
                num = v.pattachment3Count;
                price = v.procurementPrice;
                columnName1 = "pattachment3Count";
                columnName2 = "procurementPrice";
                tableName = "initChildPA3Table_" + v.dataId;
            } else if (typeIndex == 9){
                dataId = v.pattachment4Id;
                num = v.pattachment4Count;
                price = v.procurementPrice;
                columnName1 = "pattachment4Count";
                columnName2 = "procurementPrice";
                tableName = "initChildPA4Table_" + v.dataId;
            }
            $("#initChildProductTable_" + dataId).bootstrapTable('expandRow', 0);
            overAllIds.indexOf(typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue) == -1 ?
                overAllIds.push(typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue) : -1;
            numberMap.set(typeIndex + "_" + v.childId + "_" + dataId+ "_" + v.dataId + "_" + v.levelValue,num);

            var price1 = setRowTotalPrice(columnName1,columnName2,tableName,v);
            console.log("v.totalPrice1=" + price1);
            priceMap.set(typeIndex + "_" + v.childId + "_" + dataId+ "_" + v.dataId + "_" + v.levelValue,price1);

            console.log("checked supplierId=" + v.supplierId);
            supplierMap.set(v.supplierId,typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue);
            string3_parent = v.string3;
            deptName_parent = v.deptName;
            haoliMap.set(id,id);
            guishudanwei.set(v.guishudanwei,v.guishudanwei);
        });
    }else{
        $.each(datas,function(i,v){
            console.log("typeIndex:" + typeIndex)
            console.log("v:" + v)
            var dataId = "";
            if (typeIndex == 1) {
                dataId = v.productId;
            } else if (typeIndex == 2) {
                dataId = v.actuatorId;
            } else if (typeIndex == 3) {
                dataId = v.productRef1Id;
            } else if (typeIndex == 4) {
                dataId = v.productRef2Id;
            } else if (typeIndex == 5){
                dataId = v.pattachmentId;
            } else if (typeIndex == 6){
                dataId = v.pattachment1Id;
            } else if (typeIndex == 7){
                dataId = v.pattachment2Id;
            } else if (typeIndex == 8){
                dataId = v.pattachment3Id;
            } else if (typeIndex == 9){
                dataId = v.pattachment4Id;
            }
            overAllIds.splice(overAllIds.indexOf(typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue),1);    //删除取消选中行
            supplierMap.delete(v.supplierId);
            guishudanwei.delete(v.guishudanwei);
            priceMap.delete(typeIndex + "_" + v.childId + "_" + dataId+ "_" + v.dataId + "_" + v.levelValue);
            console.log("id:"+id)
            haoliMap.set(id,id);
        });
    }
}



initChildActuatorTable = function(index, row, $detail) {
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildActuatorTable_' + row.dataId + '"></table></div>').find('table');
    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelActuator",
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "actuatorId",
        async: false,
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("2_" + row.childId + "_" + row.actuatorId + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(2,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'actuatorId',title : 'actuatorId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},

            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'actuatorName',title : '执行器名称',editable: false},


            {field : 'actuatorNum',title : '执行器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'actuatorString6',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["actuatorNum"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["actuatorString6"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'actuatorStatus',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'actuatorBrand',
                title : '执行器品牌'
            },
            {
                field : 'actuatorManufacturer',
                title : '生产厂家'
            },
            {
                field : 'actuatorString4',
                title : '厂家代码'
            },
            {
                field : 'actuatorString1',
                title : '型号'
            },
            {
                field : 'actuatorSetupType',
                title : '安装形式',
                formatter: function(value, row, index) {
                    return $.table.selectDictLabel(setupTypeData, value);
                }
            },
            {
                field : 'actuatorOutputTorque',
                title : '输出力距'
            },
            {
                field : 'actuatorString3',
                title : '系列'
            },
            {
                field : 'actuatorActionType',
                title : '开启时间'
            },
            {
                field : 'actuatorControlCircuit',
                title : '控制电路'
            },
            {
                field : 'actuatorAdaptableVoltage',
                title : '适用电压'
            },
            {
                field : 'actuatorProtectionLevel',
                title : '防护等级'
            },
            {
                field : 'actuatorQualityLevel',
                title : '品质等级'
            },
            {
                field : 'actuatorExplosionLevel',
                title : '防爆等级'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,2,row["dataId"]);
    });
};

initChildRef1Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildRef1Table_' + row.dataId + '"></table>').find('table');

    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildRef1Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelRef1",
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "productRef1Id",
        onEditableSave: onEditableSave,
        async: false,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("3_" + row.childId + "_" + row.productRef1Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(3,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'productRef1Id',title : 'ref1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'ref1Name',title : '法兰名称',editable: false},

            {field : 'productRef1Num',title : '法兰数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'ref1String2',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["productRef1Num"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["ref1String2"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'ref1Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {field : 'model',title : '型号'},
            {field : 'ref1Specifications',title : '规格'},
            {field : 'ref1ValvebodyMaterial',title : '材质'},
            {field : 'ref1MaterialRequire',title : '材质要求'},
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,3,row["dataId"]);
    });
};



initChildRef2Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildRef2Table_' + row.dataId + '"></table>').find('table');

    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildRef2Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelRef2",
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "productRef2Id",
        async: false,
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("4_" + row.childId + "_" + row.productRef2Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(4,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'productRef2Id',title : 'ref2Id',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'ref2Name',title : '螺栓名称',editable: {type: 'text',emptytext: '空',disabled: true}},

            {field : 'productRef2Num',title : '螺栓数量',editable: {type: 'text',emptytext: '0',validate: function(v){ return numberValidate(v)}}},
            {field : 'ref1String2',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["productRef2Num"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["ref1String2"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'ref2Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {field : 'model',title : '型号'},
            {field : 'ref1Specifications',title : '规格'},
            {field : 'ref1ValvebodyMaterial',title : '材质'},
            {field : 'ref1MaterialRequire',title : '材质要求'},
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,4,row["dataId"]);
    });
};

initChildPATable = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPATable_' + row.dataId + '"></table>').find('table');
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildPATable_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelPA",
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "pattachmentId",
        async: false,
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("5_" + row.childId + "_" + row.pattachmentId + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(5,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachmentId',title : 'pattachmentId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'pattachmentCount',title : '定位器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'procurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["pattachmentCount"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["procurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'pStatus',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'bh',
                title : '商品编号'
            },
            {
                field : 'chineseName',
                title : '中文品名'
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格'
            },
            {
                field : 'chineseUnit',
                title : '中文单位'
            },
            {
                field : 'pressure',
                title : '压力'
            },
            {
                field : 'material',
                title : '材质'
            },
            {
                field : 'color',
                title : '颜色'
            },
            {
                field : 'developer',
                title : '开发人员'
            },
            {
                field : 'goodsCategory',
                title : '商品分类'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,5,row["dataId"]);
    });
};

initChildPA1Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA1Table_' + row.dataId + '"></table>').find('table');
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildPA1Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelPA1",
        method: 'post',
        sidePagination: "server",
        async: false,
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "pattachment1Id",
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("6_" + row.childId + "_" + row.pattachment1Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(6,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment1Id',title : 'pattachment1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'pattachment1Count',title : '电磁阀数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'procurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["pattachment1Count"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["procurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'p1Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'bh',
                title : '商品编号'
            },
            {
                field : 'chineseName',
                title : '中文品名'
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格'
            },
            {
                field : 'chineseUnit',
                title : '中文单位'
            },
            {
                field : 'pressure',
                title : '压力'
            },
            {
                field : 'material',
                title : '材质'
            },
            {
                field : 'color',
                title : '颜色'
            },
            {
                field : 'developer',
                title : '开发人员'
            },
            {
                field : 'goodsCategory',
                title : '商品分类'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,6,row["dataId"]);
    });
};

initChildPA2Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA2Table_' + row.dataId + '"></table>').find('table');
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildPA2Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelPA2",
        method: 'post',
        sidePagination: "server",
        async: false,
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "pattachment2Id",
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {

                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("7_" + row.childId + "_" + row.pattachment2Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(7,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment2Id',title : 'pattachment2Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'pattachment2Count',title : '回信器数数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'procurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["pattachment2Count"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["procurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'p2Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'bh',
                title : '商品编号'
            },
            {
                field : 'chineseName',
                title : '中文品名'
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格'
            },

            {
                field : 'chineseUnit',
                title : '中文单位'
            },
            {
                field : 'englishUnit',
                title : '英文单位'
            },
            {
                field : 'pressure',
                title : '压力'
            },
            {
                field : 'material',
                title : '材质'
            },

            {
                field : 'color',
                title : '颜色'
            },
            {
                field : 'developer',
                title : '开发人员'
            },
            {
                field : 'goodsCategory',
                title : '商品分类'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,7,row["dataId"]);
    });
};

initChildPA3Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA3Table_' + row.dataId + '"></table>').find('table');
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildPA3Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelPA3",
        method: 'post',
        sidePagination: "server",
        async: false,
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "pattachment3Id",
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("8_" + row.childId + "_" + row.pattachment3Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(8,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment3Id',title : 'pattachment3Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'pattachment3Count',title : '气源三连件数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'procurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["pattachment3Count"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["procurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'p3Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'bh',
                title : '商品编号'
            },
            {
                field : 'chineseName',
                title : '中文品名'
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格'
            },
            {
                field : 'chineseUnit',
                title : '中文单位'
            },
            {
                field : 'pressure',
                title : '压力'
            },
            {
                field : 'material',
                title : '材质'
            },
            {
                field : 'color',
                title : '颜色'
            },
            {
                field : 'developer',
                title : '开发人员'
            },
            {
                field : 'goodsCategory',
                title : '商品分类'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,8,row["dataId"]);
    });
};


initChildPA4Table = function(index, row, $detail) {
    //var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA4Table_' + row.dataId + '"></table>').find('table');
    var cur_table = $detail.html('<div class="table-responsive"><table class="table text-nowrap"  id="initChildPA4Table_' + row.dataId + '"></table></div>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelPA4",
        method: 'post',
        sidePagination: "server",
        async: false,
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "pattachment4Id",
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val(),
            "bizEditFlag":$("#bizEditFlag").val(),
            "procurementId": procurementId
        },
        columns: [{
            checkbox: true,
            formatter: function (i,row) {
                var disabledValue = false;
                if (disableCheckbox(row) == "-1") {
                    disabledValue = true;
                }
                var checkedValue = false;
                if($.inArray("9_" + row.childId + "_" + row.pattachment4Id + "_" + row.dataId + "_" + row.levelValue, overAllIds)!=-1){
                    showNum(9,row);
                    checkedValue = true;
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }
            }
        },{field : 'rowId',title : '序号',visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment4Id',title : 'pattachment4Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'contractNo',title : '合同编号',visible: true},
            {field : 'pattachment4Count',title : '可离合减速器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}}},
            {field : 'procurementPrice',title : '采购价',editable: false},
            {field : 'totalPrice',title : '分项金额',editable: false,formatter: function(value, row, index) {
                    var actions = [];
                    var productNum = row["pattachment4Count"];
                    productNum = $.common.isEmpty(productNum) == true ? 0 : parseFloat(productNum);
                    var procurementPrice = row["procurementPrice"];
                    procurementPrice = $.common.isEmpty(procurementPrice) == true ? 0 : parseFloat(procurementPrice);
                    var total = parseFloat(productNum * procurementPrice).toFixed(2);
                    actions.push(total);
                    return actions.join('');
                }},
            {field : 'p4Status',title : '采购状态',editable: false,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false},
            {
                field : 'bh',
                title : '商品编号'
            },
            {
                field : 'chineseName',
                title : '中文品名'
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格'
            },
            {
                field : 'chineseUnit',
                title : '中文单位'
            },
            {
                field : 'pressure',
                title : '压力'
            },
            {
                field : 'material',
                title : '材质'
            },
            {
                field : 'handlingFee',
                title : '操作费'
            },
            {
                field : 'color',
                title : '颜色'
            },
            {
                field : 'developer',
                title : '开发人员'
            },
            {
                field : 'goodsCategory',
                title : '商品分类'
            },
            // {field : 'contractSpecial',title : '特殊要求',editable: false}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,9,row["dataId"]);
    });
};

function initChildLevelListTable(index, row, $detail) {
    var levelType = row["levelType"];
    var level = row["level"];
    var dataId = row["dataId"];
    $.common.startWith()
    if ($.common.startWith(levelType,'1')) {
        initChildProductTable(index, row, $detail);
    } else if ($.common.startWith(levelType,'2')) {
        initChildActuatorTable(index, row, $detail);
    } else if ($.common.startWith(levelType,'3')) {
        initChildRef1Table(index, row, $detail);
    } else if ($.common.startWith(levelType,'4')) {
        initChildRef2Table(index, row, $detail);
    } else if ($.common.startWith(levelType,'5')) {
        initChildPATable(index, row, $detail);
    } else if ($.common.startWith(levelType,'6')) {
        initChildPA1Table(index, row, $detail);
    } else if ($.common.startWith(levelType,'7')) {
        initChildPA2Table(index, row, $detail);
    } else if ($.common.startWith(levelType,'8')) {
        initChildPA3Table(index, row, $detail);
    } else if ($.common.startWith(levelType,'9')) {
        initChildPA4Table(index, row, $detail);
    }
}
function numberValidate(value) {
    if (isNaN(value)) {
        return "必须是数字！";
    }
}

