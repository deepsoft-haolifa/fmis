package com.ruoyi.fmis.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色
 *
 * @author murphy.he
 **/
@Getter
@AllArgsConstructor
public enum RoleEnum {
    CNY("cny", "出纳员"),
    PROCESS_FK_ZJL("process_fk_zjl", "付款工作流-总经理"),
    ZGKJ("zgkj", "主管会计"),
    ;
    private String roleKey;

    private String roleName;

}
