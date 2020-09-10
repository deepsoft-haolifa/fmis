var prefixPool = ctx + "fmis/data";

$(function() {
    var options = {
        url: prefixPool + "/selectBizTestProductList",
        modalName: "采购产品",
        uniqueId: "productId",
        detailView: true,
        cache: true,
        async: false,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: false,
        showFooter: false,
        showColumns: false,
        pageSize: 10000,
        id: "bootstrap-table",
        pagination: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildTestTable(index, row, $detail);
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + "," + row.productId +  "," + row.childId + ",'bootstrap-table'" + ')"><i class="fa fa-add"></i> 发起质检</a>');
                    return actions.join('');
                }},
            {field : 'productNum',title : '数量',editable: false,width: 100},

            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'productId',title : '产品ID',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false,width:100},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'productName',title : '产品名称',editable: false,width: 100},
            {field : 'model',title : '型号',editable: false,width: 200},
            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'specifications',title : '规格',editable: false,width: 100},
            {field : 'nominalPressure',title : '压力',editable: false,width: 100},
            {field : 'valvebodyMaterial',title : '阀体',editable: false,width: 100},
            {field : 'valveElement',title : '阀芯',editable: false,width: 100},
            {field : 'sealingMaterial',title : '密封材质',editable: false,width: 100},
            {field : 'driveForm',title : '驱动形式',editable: false,width: 100},
            {field : 'connectionType',title : '连接方式',editable: false,width: 100},
            {field : 'productProcurementPrice',title : '采购价',editable: false,width: 100}
        ]
    };
    $.table.init(options);
});
function cellStyle (value, row, index) {
    return {
        width: 100,
        css: {
            'background':'yellow'
        }
    }
}
function expandChildTablePromise(dataId,paramterId,childId,tableId) {
    var promise;
    promise = new Promise(function(resolve, reject) {
        console.log("tableId2=" + tableId);
        $("#" + tableId).bootstrapTable('expandRow', $("#" + tableId).bootstrapTable('getRowByUniqueId', paramterId).rowId);
        //bootstrap-table-actuator
        //bootstrap-table-ref1
        //bootstrap-table-ref2
        //bootstrap-table-pa
        console.log("promise 1.");
        resolve();
    });
    return promise;
}

function addTest(dataId,paramterId,childId,tableId) {
    expandChildTablePromise(dataId,paramterId,childId,tableId).then(function () {
        var rowData = {
            stayId: 0,
            stayNum: 0,
            remark: "",
            createTime: "",
            createByName: ""
        };
        setTimeout(function () {
            console.log("promise 2." + paramterId);
            $("#initChildTestTableId_" + childId).bootstrapTable('append', rowData);
            console.log("promise 4." + paramterId);
        },500)

    })
}

initChildTestTable = function(index, rows, $detail) {

    var dataId = rows["dataId"];
    var paramterId = rows["productId"];
    var totalNum = rows["productNum"];

    if ($.common.isEmpty(paramterId)) {
        paramterId = rows["actuatorId"];
        totalNum = rows["actuatorNum"];
    }
    if ($.common.isEmpty(paramterId)) {
        paramterId = rows["productRef1Id"];
        totalNum = rows["productRef1Num"];
    }
    if ($.common.isEmpty(paramterId)) {
        paramterId = rows["productRef2Id"];
        totalNum = rows["productRef2Num"];
    }
    if ($.common.isEmpty(paramterId)) {
        paramterId = rows["pattachmentId"];
        totalNum = rows["pattachmentCount"];
    }
    console.log("paramterId=" + paramterId);
    var childId = rows["childId"];
    var statusId = rows["statusId"];
    var yesNum = rows["yesNum"];
    var initChildTestTableId = "initChildTestTableId_" + childId;
    var cur_table = $detail.html('<table style="table-layout:fixed" id=' + initChildTestTableId + ' data-cache="true"></table>').find('table');

    $(cur_table).bootstrapTable({
        url: ctx + "fmis/steststay/selectBizTestChildList",
        method: 'post',
        detailView: false,
        cache: true,
        rowStyle: function(row, index) {
            return {classes:'danger'};
        },
        async: false,
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        uniqueId: "testId",
        queryParams : {
            "paramterId": paramterId,
            "dataId": dataId,
            "childId": childId,
            "statusId": statusId
        },columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false},
            {field : 'saveTest',title : '操作',width: 200,visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="saveTest(' + row.rowId + "," + childId + "," + paramterId + "," + dataId + "," + statusId + "," + totalNum + "," + yesNum + ')"><i class="fa fa-save"></i> 保存</a>');
                    //actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="removeTest(' + row.rowId + "," + childId + "," + paramterId + "," + dataId + ')"><i class="fa fa-remove"></i> 删除</a>');
                    return actions.join('');
                }},
            {field : 'stayNum',title : '数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 150},
            {field : 'orderNo',title : '报检单号',editable: false,width: 200},
            {field : 'remark',title : '备注',editable: true,width: 300},
            {field : 'createTime',title : '创建时间',width: 200},
            {field : 'createByName',title : '创建人',width: 200}
        ]
    });
};

function saveTest (rowId,childId,paramterId,dataId,statusId,parentTotalNum,parentYesNum) {
    //dataId,paramterId,childId,remark,testId,yesNum,noNum

    var rows = $("#initChildTestTableId_" + childId).bootstrapTable('getData');

    //var parentTable = $("#initChildTestTableId_" + childId).bootstrapTable("getParent");

    var row;
    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        if (r.rowId == rowId) {
            row = r;
            break;
        }
    }
    var totalNum = 0;
    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        var rStayNum = r.stayNum;
        if ($.common.isEmpty(rStayNum)) {
            rStayNum = 0;
        }
        totalNum = parseFloat(FloatAdd(totalNum,rStayNum)).toFixed(0);
    }
    var stayNum = row.stayNum;
    console.log("parentTotalNum=" + parentTotalNum + " totalNum=" + totalNum);
    if ($.common.isNotEmpty(parentTotalNum)) {
        var parentTotalNumF = parseFloat(parentTotalNum).toFixed(0);
        if (parseInt(stayNum) > parseInt(parentTotalNumF - parentYesNum)) {
            $.modal.alertWarning("数量填写错误");
            return;
        }
    }
    var remark = row.remark;
    var stayId = row.stayId;

    console.log("statusId=" + statusId);
    var url = ctx + "fmis/steststay/saveTest?dataId=" + dataId + "&paramterId=" + paramterId + "&childId=" + childId + "&remark=" + remark + "&stayId=" + stayId +
        "&stayNum=" + stayNum + "&statusId=" + statusId;
    $.operate.saveModal(url,'',function(){
        //$.table.refresh();
        $("#initChildTestTableId_" + childId).bootstrapTable('refresh');
    });
}

function FloatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}

function removeTest (rowId,childId,paramterId,dataId) {
    //dataId,paramterId,childId,remark,testId,yesNum,noNum
    var rows = $("#initChildTestTableId_" + childId).bootstrapTable('getData');
    var row;
    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        if (r.rowId == rowId) {
            row = r;
            break;
        }
    }
    var remark = row.remark;
    var stayId = row.stayId;
    var url = ctx + "fmis/steststay/removeTest?stayId=" + stayId;
    $.operate.saveModal(url,'',function(){
        //$.table.refresh();
        $("#initChildTestTableId_" + childId).bootstrapTable('refresh');
    });
}

function numberValidate(value) {
    if (isNaN(value)) {
        return "必须是数字！";
    }
}
$(function() {
    var options1 = {
        url: prefixPool + "/selectBizTestActuatorList",
        modalName: "采购执行器",
        uniqueId: "actuatorId",
        id: "bootstrap-table-actuator",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: false,
        showFooter: false,
        showColumns: false,
        pageSize: 10000,
        pagination: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildTestTable(index, row, $detail);
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + "," + row.actuatorId +  "," + row.childId + ",'bootstrap-table-actuator'" + ')"><i class="fa fa-add"></i> 发起质检</a>');
                    return actions.join('');
                }},
            {field : 'actuatorNum',title : '执行器数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'actuatorId',title : 'actuatorId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'actuatorName',title : '执行器名称',editable: false,width: 150},
            {field : 'actuatorPrice',title : '执行器价格',editable: false,width: 100},
            {field : 'actuatorString6',title : '采购价',editable: false,width: 100},

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
    };
    $.table.init(options1);
});


$(function() {
    var options1 = {
        url: prefixPool + "/selectBizTestRef1List",
        modalName: "法兰",
        uniqueId: "productRef1Id",
        id: "bootstrap-table-ref1",
        cache: true,
        detailView: true,
        async: false,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: false,
        showFooter: false,
        showColumns: false,
        pageSize: 10000,
        pagination: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildTestTable(index, row, $detail);
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + "," + row.productRef1Id +  "," + row.childId  + ",'bootstrap-table-ref1'" + ')"><i class="fa fa-add"></i> 发起质检</a>');
                    return actions.join('');
                }},
            {field : 'productRef1Num',title : '法兰数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'productRef1Id',title : 'ref1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'ref1Name',title : '法兰名称',editable: false,width: 100},
            {field : 'ref1Price',title : '法兰价格',editable: false,width: 100},
            {field : 'ref1String2',title : '采购价',editable: false,width: 100},

            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},

            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {field : 'model',title : '型号',width: 100},
            {field : 'ref1Specifications',title : '规格',width: 100},
            {field : 'ref1ValvebodyMaterial',title : '材质',width: 100},
            {field : 'ref1MaterialRequire',title : '材质要求',width: 300}
        ]
    };
    $.table.init(options1);
});



$(function() {
    var options1 = {
        url: prefixPool + "/selectBizTestRef2List",
        modalName: "螺栓",
        uniqueId: "productRef2Id",
        id: "bootstrap-table-ref2",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: false,
        showFooter: false,
        showColumns: false,
        pageSize: 10000,
        pagination: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildTestTable(index, row, $detail);
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + "," + row.productRef2Id +  "," + row.childId + ",'bootstrap-table-ref2'" + ')"><i class="fa fa-add"></i> 发起质检</a>');
                    return actions.join('');
                }},
            {field : 'productRef2Num',title : '螺栓数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'productRef2Id',title : 'ref2Id',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'ref2Name',title : '螺栓名称',editable: {type: 'text',emptytext: '空',disabled: true},width: 100},
            {field : 'ref2Price',title : '螺栓价格',editable: false,width: 100},
            {field : 'ref1String2',title : '采购价',editable: false,width: 100},
            {field : 'supplierName',title : '供应商',editable: false,width: 100},
            {field : 'supplierId',title : 'supplierId',visible: false},
            {field : 'goodsTime',title : '回货时间',editable: false,width: 100},
            {field : 'model',title : '型号',width: 100},
            {field : 'ref1Specifications',title : '规格',width: 100},
            {field : 'ref1ValvebodyMaterial',title : '材质',width: 100},
            {field : 'ref1MaterialRequire',title : '材质要求',width: 300}
        ]
    };
    $.table.init(options1);
});


$(function() {
    var options1 = {
        url: prefixPool + "/selectBizTestPAList",
        modalName: "螺栓",
        uniqueId: "pattachmentId",
        id: "bootstrap-table-pa",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: false,
        showFooter: false,
        showColumns: false,
        pageSize: 10000,
        pagination: false,
        rowStyle: function(row, index) {
            return {classes:'success'};
        },
        expandFirst: true,
        onExpandRow : function(index, row, $detail) {
            initChildTestTable(index, row, $detail);
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'paType',title : '类型',editable: false,width: 100,
                formatter: function(value, row, index) {
                    var actions = [];
                    var paType = row.paType;
                    if (paType == 1) {
                        actions.push('<i class="fa fa-warning">定位器</i>');
                    } else if (paType == 2) {
                        actions.push('<i class="fa fa-warning">电磁阀</i>');
                    } else if (paType == 3) {
                        actions.push('<i class="fa fa-warning">回信器数</i>');
                    } else if (paType == 4) {
                        actions.push('<i class="fa fa-warning">气源三连件</i>');
                    } else if (paType == 5) {
                        actions.push('<i class="fa fa-warning">可离合减速器</i>');
                    }
                    return actions.join('');
                }},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + "," + row.pattachmentId +  "," + row.childId + ",'bootstrap-table-pa'" + ')"><i class="fa fa-add"></i> 发起质检</a>');
                    return actions.join('');
                }},
            {field : 'pattachmentCount',title : '数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'pattachmentId',title : 'pattachmentId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachmentPrice',title : '价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
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
    };
    $.table.init(options1);
});