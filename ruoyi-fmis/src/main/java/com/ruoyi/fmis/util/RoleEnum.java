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
    ZJJL("zjjl", "资金经理"),
    ;
    private String roleKey;

    private String roleName;

}
