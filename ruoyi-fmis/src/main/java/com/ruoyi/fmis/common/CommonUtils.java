package com.ruoyi.fmis.common;

import com.ruoyi.system.domain.SysRole;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    /**
     * Map集合模糊匹配
     * @param map  map集合
     * @param keyLike  模糊key
     * @return
     */
    public static SysRole getLikeByMap(Map<String, SysRole> map, String keyLike){
        for (Map.Entry<String, SysRole> entity : map.entrySet()) {
            if(entity.getKey().indexOf(keyLike)>-1){
                return entity.getValue();
            }

        }
        return null;
    }

}
