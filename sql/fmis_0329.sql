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
left join biz_dict d2 on d2.parent_type='specification' and d2.name=trim(t.规格) and d2.parent_id=d1.dict_id
left join biz_dict d3 on d3.parent_type='nominal_pressure' and d3.name=trim(t.公称压力) and d3.parent_id=d1.dict_id
left join biz_dict d4 on d4.parent_type='link_type' and d4.name=trim(t.连接方式) and d4.parent_id=d1.dict_id
left join biz_dict d5 on d5.parent_type='structural_type' and d5.name=trim(t.结构形式) and d5.parent_id=d1.dict_id
left join biz_dict d6 on d6.parent_type='body_material' and d6.name=trim(t.阀体材质) and d6.parent_id=d1.dict_id
left join biz_dict d7 on d7.parent_type='spool_material' and d7.name=trim(t.阀芯材质) and d7.parent_id=d1.dict_id
left join biz_dict d8 on d8.parent_type='sealing_material' and d8.name=trim(t.密封材质) and d8.parent_id=d1.dict_id
left join biz_dict d9 on d9.parent_type='drive_mode' and d9.name=trim(t.驱动形式) and d9.parent_id=d1.dict_id




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



insert into biz_customer(
record_date, company_code, area, record_code, owner_user_id, name, project_ame, contact_name, contact_position, contact_phone, contact_email, brand, info, product_info, remark, status, del_flag, area_code, source, record_type, record_num, telephone, fax, company_address, dept_id,create_by
)

select 客户初次备案日期,商务公司代码,d1.dict_value,项目备案号,'',客户名称,项目名称,客户联系人,'',手机,email,'','','','',0,0,客户区域代码,客户来源,
case trim(c.备案类别) when '客户名称' then '1' when '项目名称' then '2' else '' end,'',座机电话,联系传真,公司地址,'0','admin_1'
 from temp_customer c
left join sys_dict_data d1 on d1.dict_type='customer_area' and d1.dict_label=c.市场区域

insert into biz_actuator(
name, brand, manufacturer, setup_type, output_torque, action_type, control_circuit, adaptable_voltage, protection_level, quality_level, explosion_level, price, remark, string1, string2,status, del_flag, create_by
)
select 产品名称,执行器品牌,生产厂家,d1.dict_value,输出力距,开启时间,控制电路,适用电压,防护等级,品质等级,防爆等级,trim(价格),备注,型号,'1',0,0,'0' from temp_dd_actuator
left join sys_dict_data d1 on d1.dict_label=trim(安装形式) and d1.dict_type='actuator_setup_type'

insert into biz_product_ref(
		name, type, model, specifications, valvebody_material, material_require, price, remark, suppliers_id,  status, del_flag, create_by
	)
	select  名称,'1',型号,
	(select dict_id from biz_dict where parent_type='specification' and name=trim(规格) limit 1),
	(select dict_id from biz_dict where parent_type='body_material' and name=trim(材质) limit 1),
	材质要求,trim(单价),'','208',0,0,'0' from temp_fl_product_ref;




insert into biz_product_ref(
		name, type, model, specifications, valvebody_material, material_require, price, remark, suppliers_id,  status, del_flag, create_by
	)
	select  名称,'2',型号,
	(select dict_id from biz_dict where parent_type='specification' and name=trim(规格) limit 1),
	(select dict_id from biz_dict where parent_type='body_material' and name=trim(材质) limit 1),
	材质要求,trim(单价),'','209',0,0,'0' from temp_ls_product_ref;



delete  from
    biz_dict
WHERE

    (name,parent_id,parent_type) IN (

	select * from (
        SELECT
            name,parent_id,parent_type
        FROM
            biz_dict
        GROUP BY
            name,parent_id,parent_type
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

    GROUP BY
        name,parent_id,parent_type
    HAVING
        count(*) > 1
		) as temp1
)



update biz_product p set p.string1=(select d.code from biz_dict d where d.parent_id=1 and p.series=d.dict_id);

update biz_product set string1='D270' where string1='D';
