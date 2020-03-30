insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.规格),trim(t.规格),d1.dict_id,'',0,0,'admin_1','specification'   from temp_product t join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d2 on d2.parent_type='specification' and d2.name=trim(t.规格)
and d2.parent_id=d1.dict_id where d2.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.公称压力),trim(t.公称压力),d1.dict_id,'',0,0,'admin_2','nominal_pressure'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d3 on d3.parent_type='nominal_pressure' and d3.code=trim(t.公称压力) and d3.parent_id=d1.dict_id
where d3.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.连接方式),trim(t.连接方式),d1.dict_id,'',0,0,'admin_3','link_type'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d4 on d4.parent_type='link_type' and d4.code=trim(t.连接方式) and d4.parent_id=d1.dict_id
where d4.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.结构形式),trim(t.结构形式),d1.dict_id,'',0,0,'admin_4','structural_type'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d5 on d5.parent_type='structural_type' and d5.code=trim(t.结构形式) and d5.parent_id=d1.dict_id
where d5.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.阀体材质),trim(t.阀体材质),d1.dict_id,'',0,0,'admin_5','body_material'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d6 on d6.parent_type='body_material' and d6.code=trim(t.阀体材质) and d6.parent_id=d1.dict_id
where d6.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.阀芯材质),trim(t.阀芯材质),d1.dict_id,'',0,0,'admin_6','body_material'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d7 on d7.parent_type='spool_material' and d7.code=trim(t.阀芯材质) and d7.parent_id=d1.dict_id
where d7.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.密封材质),trim(t.密封材质),d1.dict_id,'',0,0,'admin_7','sealing_material'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d8 on d8.parent_type='sealing_material' and d8.code=trim(t.密封材质) and d8.parent_id=d1.dict_id
where d8.dict_id is null;


insert into biz_dict(
name, code, parent_id, remark, status, del_flag, create_by, parent_type
)
select distinct trim(t.驱动形式),trim(t.驱动形式),d1.dict_id,'',0,0,'admin_8','drive_mode'
from temp_product t
join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d9 on d9.parent_type='drive_mode' and d9.code=trim(t.驱动形式) and d9.parent_id=d1.dict_id
where d9.dict_id is null;




insert into biz_product(
name, series, model, specifications, nominal_pressure, connection_type, valvebody_material, valve_material, sealing_material, valve_element, drive_form, price, supplier, remark, status, del_flag, create_by, structural_style, new_supplier, procurement_price, cost_price
)

select  t.产品名称,d1.dict_id,t.产品型号,d2.dict_id,d3.dict_id,d4.dict_id,
d6.dict_id,'valve_material',d8.dict_id,d7.dict_id,d9.dict_id,trim(t.销售底价),'',trim(t.备注),'0','0','admin1',d5.dict_id,trim(t.供应商名称),
case trim(t.采购价) when '' then 0 else trim(t.采购价) end, case trim(t.成本价) when '' then 0 else trim(t.成本价) end
 from temp_product t
 join biz_dict d1 on d1.parent_id='1' and d1.code=trim(t.系列)
left join biz_dict d2 on d2.parent_type='specification' and d2.code=trim(t.规格) and d2.parent_id=d1.dict_id
left join biz_dict d3 on d3.parent_type='nominal_pressure' and d3.code=trim(t.公称压力) and d3.parent_id=d1.dict_id
left join biz_dict d4 on d4.parent_type='link_type' and d4.code=trim(t.连接方式) and d4.parent_id=d1.dict_id
left join biz_dict d5 on d5.parent_type='structural_type' and d5.code=trim(t.结构形式) and d5.parent_id=d1.dict_id
left join biz_dict d6 on d6.parent_type='body_material' and d6.code=trim(t.阀体材质) and d6.parent_id=d1.dict_id
left join biz_dict d7 on d7.parent_type='spool_material' and d7.code=trim(t.阀芯材质) and d7.parent_id=d1.dict_id
left join biz_dict d8 on d8.parent_type='sealing_material' and d8.code=trim(t.密封材质) and d8.parent_id=d1.dict_id
left join biz_dict d9 on d9.parent_type='drive_mode' and d9.code=trim(t.驱动形式) and d9.parent_id=d1.dict_id




delete  from
    biz_dict
WHERE
 parent_type='specification' and
    (code, parent_id) IN (

	select * from (
        SELECT
            code,
            parent_id
        FROM
            biz_dict
			where parent_type='specification'
        GROUP BY
            code,
            parent_id
        HAVING
            count(*) > 1
			) as temp

    )
AND dict_id NOT IN (

	select * from (

    SELECT
        min(dict_id)
    FROM
        biz_dict
		where parent_type='specification'
    GROUP BY
        code,
        parent_id
    HAVING
        count(*) > 1
		) as temp1
)