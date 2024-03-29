var prefixPool = ctx + "fmis/data";





function expandChildTablePromise(dataId,contractNo,paramterId,childId,stayId,tableId) {
    var promise;
    promise = new Promise(function(resolve, reject) {
        console.log("tableId2=" + tableId);
        $("#" + tableId).bootstrapTable('expandRow', $("#" + tableId).bootstrapTable('getRowByUniqueId', stayId).rowId);
        //bootstrap-table-actuator
        //bootstrap-table-ref1
        //bootstrap-table-ref2
        //bootstrap-table-pa
        console.log("promise 1.");
        resolve();
    });
    return promise;
}
function FloatAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (arg1*m+arg2*m)/m;
}
function addTest(dataId,contractNo,paramterId,childId,stayId,tableId) {
    expandChildTablePromise(dataId,contractNo,paramterId,childId,stayId,tableId).then(function () {
        var rowData = {
            testId: 0,
            yesNum: 0,
            noNum: 0,
            stayId: stayId,
            contractNo: contractNo,
            remark: "",
            createTime: "",
            createByName: ""
        };
        setTimeout(function () {
            console.log("promise 2." + paramterId);
            console.log("contractNo 2." + contractNo);
            $("#initChildTestTableId_" + stayId).bootstrapTable('append', rowData);
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
    var stayNum = rows["stayNum"];

    console.log("paramterId=" + paramterId + " stayNum=" + stayNum);
    var childId = rows["childId"];
    var statusId = rows["statusId"];
    var contractNo = rows["contractNo"];
    var stayId = rows["stayId"];
    var initChildTestTableId = "initChildTestTableId_" + stayId;
    var cur_table = $detail.html('<table style="table-layout:fixed" id=' + initChildTestTableId + ' data-cache="true"></table>').find('table');
    console.log("contractNo=" + contractNo);
    $(cur_table).bootstrapTable({
        url: ctx + "fmis/procurementtest/selectBizTestChildList",
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
            "stayId": stayId,
            "contractNo": contractNo,
            "statusId": statusId
        },columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'testId',title : 'id',visible: false},
            {field : 'statusId',title : 'statusId',visible: false},
            {field : 'contractNo',title : 'contractNo',visible: false},
            {field : 'saveTest',title : '操作',width: 200,visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="saveTest(' + row.rowId + "," + childId + "," + paramterId + "," + dataId + ",'" + contractNo + "'," + statusId + "," + stayId + "," + stayNum + ')"><i class="fa fa-save"></i> 保存</a>');
                    if(Number(row.testId) > 0) {
                        actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="uploadTestFile(' + row.testId + "," + 4 + ')"><i class="fa fa-save"></i> 上传附件</a>');
                        actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="viewTestFile(' + row.testId + "," + 4 + ')"><i class="fa fa-save"></i> 查看附件</a>');
                    }
                    return actions.join('');
                }},
            {field : 'yesNum',title : '合格数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 150},
            {field : 'noNum',title : '不合格数量',editable: {type: 'text',validate: function(v){ return numberValidate(v)}},width: 150},

            {field : 'remark',title : '不合格原因',editable: true,width: 300},
            {field : 'createTime',title : '创建时间',width: 200},
            {field : 'createByName',title : '创建人',width: 200}
        ]
    });
};


function uploadTestFile(bizId, fileType) {
    var url = ctx + "fmis/file/upload/view?fileType="+fileType+"&bizId="+bizId;
    var widthNum = this.innerWidth - 50;
    var heigthNum = this.innerHeight - 50;
    $.modal.open("上传", url,widthNum, heigthNum,function (index, layero) {
        var iframeWin = layero.find('iframe')[0];
        iframeWin.contentWindow.submitHandler(index, layero);
    });
}

function viewTestFile(bizId, fileType) {
    var url = ctx + "fmis/file/list/view?bizId="+bizId+"&fileType="+fileType;
    var widthNum = this.innerWidth - 50;
    var heigthNum = this.innerHeight - 50;
    $.modal.openNoEnter("附件查看", url,widthNum, heigthNum,function () {
        $.modal.closeAll();
    });
}


function saveTest (rowId,childId,paramterId,dataId,contractNo,statusId,stayId,stayNum) {
    //dataId,paramterId,childId,remark,testId,yesNum,noNum
    var rows = $("#initChildTestTableId_" + stayId).bootstrapTable('getData');
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
        var yesNum = r.yesNum;
        var noNum = r.noNum;
        var id = r.testId;
        console.log("id=" + id);
        totalNum = parseFloat(FloatAdd(totalNum,FloatAdd(yesNum,noNum))).toFixed(0);
        /*if ($.common.isEmpty(id) || id == 0) {
            totalNum = parseFloat(FloatAdd(totalNum,FloatAdd(yesNum,noNum))).toFixed(0);
        }*/

    }
    console.log("totalNum=" + totalNum + " totalNumAll=" + stayNum);
    if (parseInt(totalNum) > parseInt(stayNum)) {
        $.modal.alertWarning("数量填写错误");
        return;
    }
    var remark = row.remark;
    var testId = row.testId;
    var yesNum = row.yesNum;
    var noNum = row.noNum;
    console.log("testId=" + testId);
    if (!$.common.isEmpty(testId) && testId != 0) {
        $.modal.alertWarning("不能重复保存");
        return;
    }
    if (parseInt(yesNum) == 0 && parseInt(noNum) == 0) {
        $.modal.alertWarning("数量不能为0");
        return;
    }
    console.log("yesNum=" + yesNum + " noNum=" + noNum + yesNum == 0);
    console.log("yesNum=" + yesNum + " remark=" + remark);
    var url = ctx + "fmis/procurementtest/saveTest?dataId=" + dataId + "&paramterId=" + paramterId + "&childId=" + childId + "&remark=" + remark + "&testId=" + testId +
        "&yesNum=" + yesNum + "&noNum=" + noNum + "&statusId=" + statusId + "&stayId=" + stayId + "&contractNo=" + contractNo;
    $.operate.saveModal(url,'',function(){
        //$.table.refresh();
        $("#initChildTestTableId_" + stayId).bootstrapTable('refresh');

    });
}
function removeTest (rowId,childId,paramterId,dataId,stayId) {
    //dataId,paramterId,childId,remark,testId,yesNum,noNum
    var rows = $("#initChildTestTableId_" + stayId).bootstrapTable('getData');
    var row;
    for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        if (r.rowId == rowId) {
            row = r;
            break;
        }
    }
    var remark = row.remark;
    var testId = row.testId;
    var yesNum = row.yesNum;
    var noNum = row.noNum;
    console.log("yesNum=" + yesNum + " remark=" + remark);
    var url = ctx + "fmis/procurementtest/removeTest?testId=" + testId;
    $.operate.saveModal(url,'',function(){
        //$.table.refresh();
        $("#initChildTestTableId_" + stayId).bootstrapTable('refresh');
    });
}

function numberValidate(value) {
    if (isNaN(value)) {
        return "必须是数字！";
    }
}

$(function() {
    var options = {
        url: prefixPool + "/selectBizTestProductList",
        modalName: "采购产品",
        uniqueId: "stayId",
        detailView: true,
        cache: true,
        async: false,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: true,
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

        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        queryParams: {
            "dataId": $("#dataId").val(),
            "level": $("#level").val(),
            "queryStatus": $("#queryStatus").val(),
            "orderNo": $("#orderNo").val(),
            "contractNo": $("#contractNo").val(),
            "procurementNo": $("#procurementNo").val()
        },
        onLoadSuccess : function (data) {
            if (data.total == 0) {
                //隐藏此窗口
                //$.destroy("bootstrap-table-ref2");
                $("#bootstrap-table").hide();
                //$('#bootstrap-table').bootstrapTable('destroy');
            }
        },
        columns: [
            {field : 'rowId',title : '序号3',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + ",'"+ row.contractNo + "'," + row.productId +  "," + row.childId +  "," + row.stayId + ",'bootstrap-table'" + ')"><i class="fa fa-add"></i> 添加</a>');
                    return actions.join('');
                }},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'productNum',title : '数量',editable: false,width: 100,visible: false},
            {field : 'stayNum',title : '需检测数量',editable: false,width: 100},
            {field : 'yesNum',title : '已检测合格数量',editable: false,width: 100},
            {field : 'noNum',title : '已检测未合格数量',editable: false,width: 100},

            {field : 'productId',title : '产品ID',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false,width:100},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'procurementId',title : 'childId',visible: false},

            {field : 'stayCreateTime',title : '发起时间',editable: false,width: 100},
            {field : 'contractNo',title : '销售合同号',editable: false,width: 100},
            {field : 'procurementNo',title : '采购合同号',editable: false,width: 100},
            {field : 'orderNo',title : '报检单号',editable: false,width: 100},

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
            {field : 'connectionType',title : '连接方式',editable: false,width: 100}
        ]
    };

    //$.table.init(options);
    $("#bootstrap-table").bootstrapTable(options);


    var options1 = {
        url: prefixPool + "/selectBizTestActuatorList",
        modalName: "采购执行器",
        uniqueId: "stayId",
        id: "bootstrap-table-actuator",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: true,
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
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        queryParams: {
            "dataId": $("#dataId").val(),
            "level": $("#level").val(),
            "queryStatus": $("#queryStatus").val(),
            "orderNo": $("#orderNo").val(),
            "contractNo": $("#contractNo").val(),
            "procurementNo": $("#procurementNo").val()
        },
        onLoadSuccess : function (data) {
            if (data.total == 0) {
                //隐藏此窗口
                //$.destroy("bootstrap-table-ref2");
                $("#bootstrap-table-actuator").hide();
            }
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + ",'"+ row.contractNo + "'," + row.actuatorId +  "," + row.childId + "," + row.stayId + ",'bootstrap-table-actuator'" + ')"><i class="fa fa-add"></i> 添加</a>');
                    return actions.join('');
                }},
            {field : 'actuatorNum',title : '执行器数量',editable: false,width: 100,visible: false},
            {field : 'stayNum',title : '需检测数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'actuatorId',title : 'actuatorId',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'stayCreateTime',title : '发起时间',editable: false,width: 100},
            {field : 'contractNo',title : '销售合同号',editable: false,width: 100},
            {field : 'procurementNo',title : '采购合同号',editable: false,width: 100},
            {field : 'orderNo',title : '报检单号',editable: false,width: 100},
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
    //$.table.init(options1);
    $("#bootstrap-table-actuator").bootstrapTable(options1);

    var options2 = {
        url: prefixPool + "/selectBizTestRef1List",
        modalName: "法兰",
        uniqueId: "stayId",
        id: "bootstrap-table-ref1",
        cache: true,
        detailView: true,
        async: false,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: true,
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
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        queryParams: {
            "dataId": $("#dataId").val(),
            "level": $("#level").val(),
            "queryStatus": $("#queryStatus").val(),
            "orderNo": $("#orderNo").val(),
            "contractNo": $("#contractNo").val(),
            "procurementNo": $("#procurementNo").val()
        },
        onLoadSuccess : function (data) {
            if (data.total == 0) {
                //隐藏此窗口
                //$.destroy("bootstrap-table-ref2");
                $("#bootstrap-table-ref1").hide();
            }
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + ",'"+ row.contractNo + "'," + row.productRef1Id +  "," + row.childId + "," + row.stayId  + ",'bootstrap-table-ref1'" + ')"><i class="fa fa-add"></i> 添加</a>');
                    return actions.join('');
                }},
            {field : 'productRef1Num',title : '法兰数量',editable: false,width: 100,visible: false},
            {field : 'stayNum',title : '需检测数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'productRef1Id',title : 'ref1Id',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'stayCreateTime',title : '发起时间',editable: false,width: 100},
            {field : 'contractNo',title : '销售合同号',editable: false,width: 100},
            {field : 'procurementNo',title : '采购合同号',editable: false,width: 100},
            {field : 'orderNo',title : '报检单号',editable: false,width: 100},
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
    //$.table.init(options2);
    $("#bootstrap-table-ref1").bootstrapTable(options2);

    var options3 = {
        url: prefixPool + "/selectBizTestRef2List",
        modalName: "螺栓",
        uniqueId: "stayId",
        id: "bootstrap-table-ref2",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: true,
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
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        queryParams: {
            "dataId": $("#dataId").val(),
            "level": $("#level").val(),
            "queryStatus": $("#queryStatus").val(),
            "orderNo": $("#orderNo").val(),
            "contractNo": $("#contractNo").val(),
            "procurementNo": $("#procurementNo").val()
        },
        onLoadSuccess : function (data) {
            if (data.total == 0) {
                //隐藏此窗口
                //$.destroy("bootstrap-table-ref2");
                $("#bootstrap-table-ref2").hide();
            }
        },
        columns: [
            {field : 'rowId',title : '序号',width: 50,visible: true,formatter:function(value,row,index){row.rowId = index;return index+1;}},
            {field : 'addTest',title : '操作',visible: true,formatter: function(value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + ",'"+ row.contractNo + "'," + row.productRef2Id +  "," + row.childId + "," + row.stayId + ",'bootstrap-table-ref2'" + ')"><i class="fa fa-add"></i> 添加</a>');
                    return actions.join('');
                }},
            {field : 'productRef2Num',title : '螺栓数量',editable: false,width: 100,visible: false},
            {field : 'stayNum',title : '需检测数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'productRef2Id',title : 'ref2Id',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'stayCreateTime',title : '发起时间',editable: false,width: 100},
            {field : 'contractNo',title : '销售合同号',editable: false,width: 100},
            {field : 'procurementNo',title : '采购合同号',editable: false,width: 100},
            {field : 'orderNo',title : '报检单号',editable: false,width: 100},
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
    //$.table.init(options3);
    $("#bootstrap-table-ref2").bootstrapTable(options3);


    var options4 = {
        url: prefixPool + "/selectBizTestPAList",
        modalName: "螺栓",
        uniqueId: "stayId",
        id: "bootstrap-table-pa",
        cache: true,
        async: false,
        detailView: true,
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showHeader: true,
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
        method: 'post',
        sidePagination: "server",
        contentType : "application/x-www-form-urlencoded",
        queryParams: {
            "dataId": $("#dataId").val(),
            "level": $("#level").val(),
            "queryStatus": $("#queryStatus").val(),
            "orderNo": $("#orderNo").val(),
            "contractNo": $("#contractNo").val(),
            "procurementNo": $("#procurementNo").val()
        },
        onLoadSuccess : function (data) {
            if (data.total == 0) {
                //隐藏此窗口
                //console.log("pa data=" + JSON.stringify(data));
                $("#bootstrap-table-pa").hide();
            }
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
                    actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="addTest(' + row.dataId + ",'"+ row.contractNo + "'," + row.pattachmentId +  "," + row.childId + "," + row.stayId + ",'bootstrap-table-pa'" + ')"><i class="fa fa-add"></i> 添加</a>');
                    return actions.join('');
                }},
            {field : 'pattachmentCount',title : '数量',editable: false,width: 100,visible: false},
            {field : 'stayNum',title : '需检测数量',editable: false,width: 100},
            {field : 'yesNum',title : '合格数量',editable: false,width: 100},
            {field : 'noNum',title : '不合格数量',editable: false,width: 100},
            {field : 'stayId',title : 'stayId',visible: false},
            {field : 'pattachmentId',title : 'pattachmentId',visible: false},
            {field : 'dataId',title : 'dataId',visible: false},
            {field : 'levelValue',title : 'levelValue',visible: false},
            {field : 'procurementId',title : 'childId',visible: false},
            {field : 'statusId',title : 'statusId',visible: false,width:100},
            {field : 'childId',title : 'childId',visible: false},
            {field : 'pattachmentPrice',title : '价格',editable: false,width: 100},
            {field : 'procurementPrice',title : '采购价',editable: false,width: 100},
            {field : 'supplierName',title : '供应商',editable: false,width: 100},

            {field : 'stayCreateTime',title : '发起时间',editable: false,width: 100},
            {field : 'contractNo',title : '销售合同号',editable: false,width: 100},
            {field : 'procurementNo',title : '采购合同号',editable: false,width: 100},
            {field : 'orderNo',title : '报检单号',editable: false,width: 100},

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
                field : 'chineseUnit',
                title : '中文单位',width: 100
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
    $("#bootstrap-table-pa").bootstrapTable(options4);
});