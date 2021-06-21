package com.ruoyi.fmis.finance.domain.vo.export;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author murphy.he
 **/
@SuppressWarnings("rawtypes")
public class BillTypeConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        String value = "";
        String str = cellData.getStringValue();
        if ("收款".equals(str)) {
            value = "1";
        } else if ("付款".equals(str)) {
            value = "2";
        }
        return value;
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty,
                                       GlobalConfiguration globalConfiguration) {
        String str = "";
        if ("1".equals(value)) {
            str = "收款";
        } else if ("2".equals(value)) {
            str = "付款";
        }
        return new CellData(str);
    }
}

