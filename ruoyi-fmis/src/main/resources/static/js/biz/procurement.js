var prefixPool = ctx + "fmis/procurementpool";

log.info("loading procurment...")

$(function() {
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
        columns: [{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {
            checkbox: true
            },
            {
                field : 'dataId',
                title : 'ID',
                visible: false
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
                field : 'customerName',
                title : '客户'
            },
            {
                field : 'datetime1',
                title : '签订日期'
            },
            {
                field : 'string3',
                title : '供方'
            },
            {
                field : 'string4',
                title : '签订地点'
            },
            {
                field : 'string5',
                title : '含发票'
            },
            {
                field : 'string6',
                title : '发货时间'
            },
            {
                field : 'string7',
                title : '运输方式'
            },
            {
                field : 'string8',
                title : '付款方式'
            },
            {
                field : 'string9',
                title : '交货地点'
            },
            {
                field : 'string10',
                title : '运费承担'
            },
            {
                field : 'string11',
                title : '收货人'
            },
            {
                field : 'string12',
                title : '收货电话'
            },
            {
                field : 'price1',
                title : '总价'
            }
        ]
    };
    $.table.init(options);
});
var overAllIds = new Array();  //全局数组
var numberMap = new Map();

var supplierMap = new Map();

/**
 * 展开父子表
 */
function initExpandRow() {
    setTimeout(function () {

        var repeartMap = new Map();
        console.log("initExpandRow...");
        for (var i = 0; i < overAllIds.length; i++) {
            var pathId = overAllIds[i];
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
            console.log("pathId22=" + pathId);
            repeartMap.set(repeartKey,i);
            $("#bootstrap-table").bootstrapTable('expandRow', $("#bootstrap-table").bootstrapTable('getRowByUniqueId', parentDataId).rowId);
            var levelUniqueId = "";
            console.log("pathIds1=" + pathIds);
            if (levelValue == "A") {
                levelUniqueId = type + "1";
            } else if (levelValue == "B") {
                levelUniqueId = type + "2";
            } else if (levelValue == "C") {
                levelUniqueId = type + "3";
            }
            var initChildLevelTableId = "initChildLevelTable_" + parentDataId;
            expandChildLevelTablePromise(initChildLevelTableId,levelUniqueId).then(function () {
            })

            //展开合同类别
            var bizEditFlag = $("#bizEditFlag").val();
            if (bizEditFlag == 2) {

            }
            console.log("bizEditFlag=" + bizEditFlag);
            if (bizEditFlag == 0) {
                var paramterSupplierId = parent.$('#paramterSupplierId').val();
                console.log("paramterSupplierId=" + paramterSupplierId);
                //增加的时候 把供应商
                $("#string6").find("option[value='" + paramterSupplierId + "']").attr("selected",true);
            }
        }
    }, 500);

}

function expandChildLevelTablePromise(initChildLevelTableId,levelUniqueId) {
    var promise;
    promise = new Promise(function(resolve, reject) {
        $("#" + initChildLevelTableId).on("load-success.bs.table",{unid: levelUniqueId},(function(e){
            $("#" + initChildLevelTableId).bootstrapTable('expandRow', $("#" + initChildLevelTableId).bootstrapTable('getRowByUniqueId', e.data.unid).rowId);
        }));
        resolve();
    });
    return promise;
}

function cellStyle (value, row, index) {
    return {
        width: 300,
        css: {
            'background':'red'
        }
    }
}

initChildLevelTable = function(index, row, $detail) {

    var initChildLevelTableId = "initChildLevelTable_" + row.dataId;
    var cur_table = $detail.html('<table style="table-layout:fixed" id=' + initChildLevelTableId + ' data-cache="true"></table>').find('table');
    var dataId = row["dataId"];

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevel",
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
            "supplierId":$("#string6").val()

        },
        onExpandRow : function(index, row, $detail) {
            initChildLevelListTable(index, row, $detail);
        },columns: [
            {field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
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

}


//保存数量
function onEditableSave (field, row, oldValue, $el) {
    if (field == "productNum") {
        numberMap.set("1_" + row.childId + "_" + row.productId + "_" + row.dataId + "_" + row.levelValue,row.productNum);
    } else if (field == "actuatorNum") {
        numberMap.set("2_" + row.childId + "_" + row.actuatorId + "_" + row.dataId + "_" + row.levelValue,row.actuatorNum);
    } else if (field == "productRef1Num") {
        numberMap.set("3_" + row.childId + "_" + row.productRef1Id + "_" + row.dataId + "_" + row.levelValue,row.productRef1Num);
    } else if (field == "productRef2Num") {
        numberMap.set("4_" + row.childId + "_" + row.productRef2Id + "_" + row.dataId + "_" + row.levelValue,row.productRef2Num);
    } else if (field == "pattachmentCount") {
        numberMap.set("5_" + row.childId + "_" + row.pattachmentId + "_" + row.dataId + "_" + row.levelValue,row.pattachmentCount);
    } else if (field == "pattachment1Count") {
        numberMap.set("6_" + row.childId + "_" + row.pattachment1Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment1Count);
    } else if (field == "pattachment2Count") {
        numberMap.set("7_" + row.childId + "_" + row.pattachment2Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment2Count);
    } else if (field == "pattachment3Count") {
        numberMap.set("8_" + row.childId + "_" + row.pattachment3Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment3Count);
    } else if (field == "pattachment4Count") {
        numberMap.set("9_" + row.childId + "_" + row.pattachment4Id + "_" + row.dataId + "_" + row.levelValue,row.pattachment4Count);
    }


}

function showNum(type,row) {
    if (type == 1) {
        var num = numberMap.get(type + "_" + row.childId + "_"  + row.productId + "_" + row.dataId + "_" + row.levelValue);
        if (num != null) {
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
    var cur_table = $detail.html('<div class="col-sm-12 select-table table-striped"><table style="table-layout:fixed" id="initChildProductTable" ></table></div>').find('table');
    $(cur_table).bootstrapTable({
        url: ctx + "fmis/data/listLevelProduct",
        method: 'post',
        async: false,
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "productId",
        cache: true,
        onEditableSave: onEditableSave,
        queryParams : {
            "level": row["level"],
            "dataStatus": $("#dataStatus").val(),
            "dataId": row["dataId"],
            "supplierId":$("#string6").val()
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
                }
                return {
                    disabled: disabledValue,
                    checked : checkedValue
                }

            }
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},

            {field : 'productId',title : '产品ID',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'productName',title : '产品名称',editable: false,width: 100},
            {field : 'model',title : '型号',editable: false,width: 200},
            {field : 'productNum',title : '数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 80},
            {field : 'productStatus',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'specifications',title : '规格',editable: false,width: 100},
            {field : 'nominalPressure',title : '压力',editable: false,width: 100},
            {field : 'valvebodyMaterial',title : '阀体',editable: false,width: 100},
            {field : 'valveElement',title : '阀芯',editable: false,width: 100},
            {field : 'sealingMaterial',title : '密封材质',editable: false,width: 100},
            {field : 'driveForm',title : '驱动形式',editable: false,width: 100},
            {field : 'connectionType',title : '连接方式',editable: false,width: 100},
            {field : 'productProcurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'goodsTime',title : '回货时间',editable: false,width: 100}


        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,1);
    });
};




function examine(type,datas,typeIndex){
    if(type.indexOf('uncheck')==-1){
        $.each(datas,function(i,v){
            // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
            var dataId = "";
            var num = "0";
            if (typeIndex == 1) {
                dataId = v.productId;
                num = v.productNum;
            } else if (typeIndex == 2) {
                dataId = v.actuatorId;
                num = v.actuatorNum;
            } else if (typeIndex == 3) {
                dataId = v.productRef1Id;
                num = v.productRef1Num;
            } else if (typeIndex == 4) {
                dataId = v.productRef2Id;
                num = v.productRef2Num;
            } else if (typeIndex == 5){
                dataId = v.pattachmentId;
                num = v.pattachmentCount;
            } else if (typeIndex == 6){
                dataId = v.pattachment1Id;
                num = v.pattachment1Count;
            } else if (typeIndex == 7){
                dataId = v.pattachment2Id;
                num = v.pattachment2Count;
            } else if (typeIndex == 8){
                dataId = v.pattachment3Id;
                num = v.pattachment3Count;
            } else if (typeIndex == 9){
                dataId = v.pattachment4Id;
                num = v.pattachment4Count;
            }
            $("#initChildProductTable").bootstrapTable('expandRow', 0);
            overAllIds.indexOf(typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue) == -1 ?
                overAllIds.push(typeIndex + "_" + v.childId + "_" + dataId + "_" + v.dataId + "_" + v.levelValue) : -1;
            numberMap.set(typeIndex + "_" + v.childId + "_" + dataId+ "_" + v.dataId + "_" + v.levelValue,num);

            supplierMap.set(v.supplierId,typeIndex + "_" + v.childId + "_" + dataId+ "_" + v.dataId + "_" + v.levelValue);

        });
    }else{
        $.each(datas,function(i,v){
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
        });
    }
}



initChildActuatorTable = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildActuatorTable"></table>').find('table');
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'actuatorId',title : 'actuatorId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'actuatorName',title : '执行器名称',editable: false,width: 150},
            {field : 'actuatorPrice',title : '执行器价格',editable: false,width: 100},
            {field : 'actuatorString6',title : '采购价',editable: false,width: 100},
            {field : 'actuatorNum',title : '执行器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'actuatorStatus',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'actuatorBrand',
                title : '执行器品牌',width: 100
            },
            {
                field : 'actuatorManufacturer',
                title : '生产厂家',width: 200
            },
            {
                field : 'actuatorString4',
                title : '厂家代码',width: 100
            },
            {
                field : 'actuatorString1',
                title : '型号',width: 200
            },
            {
                field : 'actuatorSetupType',
                title : '安装形式',
                formatter: function(value, row, index) {
                    return $.table.selectDictLabel(setupTypeData, value);
                },width: 60
            },
            {
                field : 'actuatorOutputTorque',
                title : '输出力距',width: 60
            },
            {
                field : 'actuatorString3',
                title : '系列',width: 60
            },
            {
                field : 'actuatorActionType',
                title : '开启时间',width: 100
            },
            {
                field : 'actuatorControlCircuit',
                title : '控制电路',width: 100
            },
            {
                field : 'actuatorAdaptableVoltage',
                title : '适用电压',width: 60
            },
            {
                field : 'actuatorProtectionLevel',
                title : '防护等级',width: 100
            },
            {
                field : 'actuatorQualityLevel',
                title : '品质等级',width: 30
            },
            {
                field : 'actuatorExplosionLevel',
                title : '防爆等级',width: 50
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,2);
    });
};

initChildRef1Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildRef1Table"></table>').find('table');
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'productRef1Id',title : 'ref1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'ref1Name',title : '法兰名称',editable: false,width: 100},
            {field : 'ref1Price',title : '法兰价格',editable: false,width: 100},
            {field : 'ref1String2',title : '采购价',editable: false,width: 100},
            {field : 'productRef1Num',title : '法兰数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'ref1Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {field : 'model',title : '型号',width: 100},
            {field : 'ref1Specifications',title : '规格',width: 100},
            {field : 'ref1ValvebodyMaterial',title : '材质',width: 100},
            {field : 'ref1MaterialRequire',title : '材质要求',width: 300}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,3);
    });
};



initChildRef2Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildRef2Table"></table>').find('table');
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'productRef2Id',title : 'ref2Id',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'ref2Name',title : '螺栓名称',editable: {type: 'text',emptytext: '空',disabled: true},width: 100},
            {field : 'ref2Price',title : '螺栓价格',editable: false,width: 100},
            {field : 'ref1String2',title : '采购价',editable: false,width: 100},
            {field : 'productRef2Num',title : '螺栓数量',editable: {type: 'text',emptytext: '0',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'ref2Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {field : 'model',title : '型号',width: 100},
            {field : 'ref1Specifications',title : '规格',width: 100},
            {field : 'ref1ValvebodyMaterial',title : '材质',width: 100},
            {field : 'ref1MaterialRequire',title : '材质要求',width: 300}
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,4);
    });
};

initChildPATable = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPATable"></table>').find('table');
    console.log("dataId1=" + row["dataId"]);
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachmentId',title : 'pattachmentId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachmentPrice',title : '定位器价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'pattachmentCount',title : '定位器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'pStatus',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'bh',
                title : '商品编号',width: 100
            },
            {
                field : 'chineseName',
                title : '中文品名',width: 100
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格',width: 100
            },
            {
                field : 'englishName',
                title : '英文品名',width: 100
            },
            {
                field : 'englishSpecifications',
                title : '英文规格',width: 100
            },
            {
                field : 'chinesePackaging',
                title : '中文包装',width: 100
            },
            {
                field : 'englishPackaging',
                title : '英文包装',width: 100
            },
            {
                field : 'chineseUnit',
                title : '中文单位',width: 100
            },
            {
                field : 'englishUnit',
                title : '英文单位',width: 100
            },
            {
                field : 'pressure',
                title : '压力',width: 100
            },
            {
                field : 'material',
                title : '材质',width: 100
            },
            {
                field : 'barCode',
                title : '条形码',width: 100
            },
            {
                field : 'customsBh',
                title : '海关编码',width: 100
            },
            {
                field : 'handlingFee',
                title : '操作费',width: 100
            },
            {
                field : 'color',
                title : '颜色',width: 100
            },
            {
                field : 'developer',
                title : '开发人员',width: 100
            },
            {
                field : 'goodsCategory',
                title : '商品分类',width: 100
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,5);
    });
};

initChildPA1Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA1Table"></table>').find('table');
    console.log("dataId1=" + row["dataId"]);
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment1Id',title : 'pattachment1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachment1Price',title : '电磁阀价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'pattachment1Count',title : '电磁阀数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'p1Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'bh',
                title : '商品编号',width: 100
            },
            {
                field : 'chineseName',
                title : '中文品名',width: 100
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格',width: 100
            },
            {
                field : 'englishName',
                title : '英文品名',width: 100
            },
            {
                field : 'englishSpecifications',
                title : '英文规格',width: 100
            },
            {
                field : 'chinesePackaging',
                title : '中文包装',width: 100
            },
            {
                field : 'englishPackaging',
                title : '英文包装',width: 100
            },
            {
                field : 'chineseUnit',
                title : '中文单位',width: 100
            },
            {
                field : 'englishUnit',
                title : '英文单位',width: 100
            },
            {
                field : 'pressure',
                title : '压力',width: 100
            },
            {
                field : 'material',
                title : '材质',width: 100
            },
            {
                field : 'barCode',
                title : '条形码',width: 100
            },
            {
                field : 'customsBh',
                title : '海关编码',width: 100
            },
            {
                field : 'handlingFee',
                title : '操作费',width: 100
            },
            {
                field : 'color',
                title : '颜色',width: 100
            },
            {
                field : 'developer',
                title : '开发人员',width: 100
            },
            {
                field : 'goodsCategory',
                title : '商品分类',width: 100
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,6);
    });
};

initChildPA2Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA2Table"></table>').find('table');
    console.log("dataId1=" + row["dataId"]);
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment2Id',title : 'pattachment2Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachment2Price',title : '回信器数价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'pattachment2Count',title : '回信器数数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'p2Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'bh',
                title : '商品编号',width: 100
            },
            {
                field : 'chineseName',
                title : '中文品名',width: 100
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格',width: 100
            },
            {
                field : 'englishName',
                title : '英文品名',width: 100
            },
            {
                field : 'englishSpecifications',
                title : '英文规格',width: 100
            },
            {
                field : 'chinesePackaging',
                title : '中文包装',width: 100
            },
            {
                field : 'englishPackaging',
                title : '英文包装',width: 100
            },
            {
                field : 'chineseUnit',
                title : '中文单位',width: 100
            },
            {
                field : 'englishUnit',
                title : '英文单位',width: 100
            },
            {
                field : 'pressure',
                title : '压力',width: 100
            },
            {
                field : 'material',
                title : '材质',width: 100
            },
            {
                field : 'barCode',
                title : '条形码',width: 100
            },
            {
                field : 'customsBh',
                title : '海关编码',width: 100
            },
            {
                field : 'handlingFee',
                title : '操作费',width: 100
            },
            {
                field : 'color',
                title : '颜色',width: 100
            },
            {
                field : 'developer',
                title : '开发人员',width: 100
            },
            {
                field : 'goodsCategory',
                title : '商品分类',width: 100
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,7);
    });
};

initChildPA3Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA3Table"></table>').find('table');
    console.log("dataId1=" + row["dataId"]);
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment3Id',title : 'pattachment3Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachment3Price',title : '气源三连件价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'pattachment3Count',title : '气源三连件数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'p3Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'bh',
                title : '商品编号',width: 100
            },
            {
                field : 'chineseName',
                title : '中文品名',width: 100
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格',width: 100
            },
            {
                field : 'englishName',
                title : '英文品名',width: 100
            },
            {
                field : 'englishSpecifications',
                title : '英文规格',width: 100
            },
            {
                field : 'chinesePackaging',
                title : '中文包装',width: 100
            },
            {
                field : 'englishPackaging',
                title : '英文包装',width: 100
            },
            {
                field : 'chineseUnit',
                title : '中文单位',width: 100
            },
            {
                field : 'englishUnit',
                title : '英文单位',width: 100
            },
            {
                field : 'pressure',
                title : '压力',width: 100
            },
            {
                field : 'material',
                title : '材质',width: 100
            },
            {
                field : 'barCode',
                title : '条形码',width: 100
            },
            {
                field : 'customsBh',
                title : '海关编码',width: 100
            },
            {
                field : 'handlingFee',
                title : '操作费',width: 100
            },
            {
                field : 'color',
                title : '颜色',width: 100
            },
            {
                field : 'developer',
                title : '开发人员',width: 100
            },
            {
                field : 'goodsCategory',
                title : '商品分类',width: 100
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,8);
    });
};


initChildPA4Table = function(index, row, $detail) {
    var cur_table = $detail.html('<table style="table-layout:fixed" id="initChildPA4Table"></table>').find('table');
    console.log("dataId1=" + row["dataId"]);
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
            "supplierId":$("#string6").val()
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
        },{field : 'rowId',title : '序号',width: 20,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'pattachment4Id',title : 'pattachment4Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachment4Price',title : '可离合减速器价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'pattachment4Count',title : '可离合减速器数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 100},
            {field : 'p4Status',title : '采购状态',editable: false,width: 100,
                formatter: formaterStatus},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {
                field : 'bh',
                title : '商品编号',width: 100
            },
            {
                field : 'chineseName',
                title : '中文品名',width: 100
            },
            {
                field : 'chineseSpecifications',
                title : '中文规格',width: 100
            },
            {
                field : 'englishName',
                title : '英文品名',width: 100
            },
            {
                field : 'englishSpecifications',
                title : '英文规格',width: 100
            },
            {
                field : 'chinesePackaging',
                title : '中文包装',width: 100
            },
            {
                field : 'englishPackaging',
                title : '英文包装',width: 100
            },
            {
                field : 'chineseUnit',
                title : '中文单位',width: 100
            },
            {
                field : 'englishUnit',
                title : '英文单位',width: 100
            },
            {
                field : 'pressure',
                title : '压力',width: 100
            },
            {
                field : 'material',
                title : '材质',width: 100
            },
            {
                field : 'barCode',
                title : '条形码',width: 100
            },
            {
                field : 'customsBh',
                title : '海关编码',width: 100
            },
            {
                field : 'handlingFee',
                title : '操作费',width: 100
            },
            {
                field : 'color',
                title : '颜色',width: 100
            },
            {
                field : 'developer',
                title : '开发人员',width: 100
            },
            {
                field : 'goodsCategory',
                title : '商品分类',width: 100
            }
        ]
    });
    $(cur_table).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
        var datas = $.isArray(rows) ? rows : [rows];
        examine(e.type,datas,9);
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