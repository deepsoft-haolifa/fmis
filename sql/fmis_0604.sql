product_logo
transport_type
supplier_type
shipping_method
valve_shaft
-- 含税不含运费
included_freight

alter table biz_process_data add column string21 varchar(100);
alter table biz_process_data add column string22 varchar(100);
alter table biz_process_data add column string23 varchar(100);
alter table biz_process_data add column string24 varchar(100);
alter table biz_process_data add column string25 varchar(100);
alter table biz_process_data add column string26 varchar(100);
alter table biz_process_data add column string27 varchar(100);
alter table biz_process_data add column string28 varchar(100);
alter table biz_process_data add column string29 varchar(100);
alter table biz_process_data add column string30 varchar(100);

alter table biz_process_data add price11 decimal(19,5);
alter table biz_process_data add price12 decimal(19,5);
alter table biz_process_data add price13 decimal(19,5);
alter table biz_process_data add price14 decimal(19,5);
alter table biz_process_data add price15 decimal(19,5);
alter table biz_process_data add price16 decimal(19,5);
alter table biz_process_data add price17 decimal(19,5);
alter table biz_process_data add price18 decimal(19,5);
alter table biz_process_data add price19 decimal(19,5);
alter table biz_process_data add price20 decimal(19,5);




--菜单整理

-- 产品管理 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品管理', '0', '10', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdF := LAST_INSERT_ID();


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品列表', @parentIdF, '10', '/fmis/product', 'C', '0', 'fmis:product:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务产品菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:product:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:product:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:product:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:product:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:product:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- 电动执行器
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('电动执行器', @parentIdF, '20', '/fmis/actuator', 'C', '0', 'fmis:actuator:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '执行器菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器查询', @parentId, '1',  '#',  'F', '0', 'fmis:actuator:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器新增', @parentId, '2',  '#',  'F', '0', 'fmis:actuator:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器修改', @parentId, '3',  '#',  'F', '0', 'fmis:actuator:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器删除', @parentId, '4',  '#',  'F', '0', 'fmis:actuator:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器导出', @parentId, '5',  '#',  'F', '0', 'fmis:actuator:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




-- 气动执行器
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('气动执行器', @parentIdF, '30', '/fmis/airActuator', 'C', '0', 'fmis:airActuator:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '执行器菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器查询', @parentId, '1',  '#',  'F', '0', 'fmis:airActuator:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器新增', @parentId, '2',  '#',  'F', '0', 'fmis:airActuator:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器修改', @parentId, '3',  '#',  'F', '0', 'fmis:airActuator:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器删除', @parentId, '4',  '#',  'F', '0', 'fmis:airActuator:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器导出', @parentId, '5',  '#',  'F', '0', 'fmis:airActuator:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- 执行器关系
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理', @parentIdF, '40', '/fmis/actuatorRef', 'C', '0', 'fmis:actuatorRef:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理查询', @parentId, '1',  '#',  'F', '0', 'fmis:actuatorRef:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理新增', @parentId, '2',  '#',  'F', '0', 'fmis:actuatorRef:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理修改', @parentId, '3',  '#',  'F', '0', 'fmis:actuatorRef:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理删除', @parentId, '4',  '#',  'F', '0', 'fmis:actuatorRef:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理导出', @parentId, '5',  '#',  'F', '0', 'fmis:actuatorRef:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 配件管理
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('配件管理', @parentIdF, '50', '/fmis/productRef', 'C', '0', 'fmis:productRef:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '产品关系菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系查询', @parentId, '1',  '#',  'F', '0', 'fmis:productRef:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系新增', @parentId, '2',  '#',  'F', '0', 'fmis:productRef:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系修改', @parentId, '3',  '#',  'F', '0', 'fmis:productRef:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系删除', @parentId, '4',  '#',  'F', '0', 'fmis:productRef:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系导出', @parentId, '5',  '#',  'F', '0', 'fmis:productRef:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 业务数据字典
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典', @parentIdF, '60', '/fmis/dict', 'C', '0', 'fmis:dict:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务数据字典菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典查询', @parentId, '1',  '#',  'F', '0', 'fmis:dict:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典新增', @parentId, '2',  '#',  'F', '0', 'fmis:dict:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典修改', @parentId, '3',  '#',  'F', '0', 'fmis:dict:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典删除', @parentId, '4',  '#',  'F', '0', 'fmis:dict:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典导出', @parentId, '5',  '#',  'F', '0', 'fmis:dict:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




-- 销售管理 SQL start
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('销售管理', '0', '20', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='销售管理' and parent_id=0 and order_num='20';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户管理', @parentIdFP, '10', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户菜单');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='客户管理' and menu_type='M' and order_num='10';

-- 客户管理
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表', @parentIdF, '10', '/fmis/customer', 'C', '0', 'fmis:customer:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户查询', @parentId, '1',  '#',  'F', '0', 'fmis:customer:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户新增', @parentId, '2',  '#',  'F', '0', 'fmis:customer:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户修改', @parentId, '3',  '#',  'F', '0', 'fmis:customer:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户删除', @parentId, '4',  '#',  'F', '0', 'fmis:customer:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户导出', @parentId, '5',  '#',  'F', '0', 'fmis:customer:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


SELECT @parentIdF :=  menu_id from sys_menu where menu_name='客户管理' and menu_type='M' and order_num='10';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪', @parentIdF, '20', '/fmis/customertrack', 'C', '0', 'fmis:customertrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户追踪菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪查询', @parentId, '1',  '#',  'F', '0', 'fmis:customertrack:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪新增', @parentId, '2',  '#',  'F', '0', 'fmis:customertrack:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪修改', @parentId, '3',  '#',  'F', '0', 'fmis:customertrack:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪删除', @parentId, '4',  '#',  'F', '0', 'fmis:customertrack:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪导出', @parentId, '5',  '#',  'F', '0', 'fmis:customertrack:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='销售管理' and parent_id=0 and order_num='20';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价管理', @parentIdFP, '20', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='报价管理' and menu_type='M' and order_num='20';


-- 报价单管理
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单列表', @parentIdF, '10', '/fmis/quotation', 'C', '0', 'fmis:quotation:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '报价单菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单查询', @parentId, '1',  '#',  'F', '0', 'fmis:quotation:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单新增', @parentId, '2',  '#',  'F', '0', 'fmis:quotation:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单修改', @parentId, '3',  '#',  'F', '0', 'fmis:quotation:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单删除', @parentId, '4',  '#',  'F', '0', 'fmis:quotation:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单导出', @parentId, '5',  '#',  'F', '0', 'fmis:quotation:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


SELECT @parentIdF :=  menu_id from sys_menu where menu_name='报价管理' and menu_type='M' and order_num='20';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪', @parentIdF, '30', '/fmis/quotationtrack', 'C', '0', 'fmis:quotationtrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '报价追踪菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪查询', @parentId, '1',  '#',  'F', '0', 'fmis:quotationtrack:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪新增', @parentId, '2',  '#',  'F', '0', 'fmis:quotationtrack:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪修改', @parentId, '3',  '#',  'F', '0', 'fmis:quotationtrack:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪删除', @parentId, '4',  '#',  'F', '0', 'fmis:quotationtrack:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价追踪导出', @parentId, '5',  '#',  'F', '0', 'fmis:quotationtrack:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单追踪', '2148', '1', '/fmis/quotationtrack', 'C', '0', 'fmis:quotationtrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');





SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='销售管理' and parent_id=0 and order_num='20';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理', @parentIdFP, '30', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='合同管理' and menu_type='M' and order_num='30';



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同列表', @parentIdF, '10', '/fmis/data', 'C', '0', 'fmis:data:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理查询', @parentId, '1',  '#',  'F', '0', 'fmis:data:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理新增', @parentId, '2',  '#',  'F', '0', 'fmis:data:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理修改', @parentId, '3',  '#',  'F', '0', 'fmis:data:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理删除', @parentId, '4',  '#',  'F', '0', 'fmis:data:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理导出', @parentId, '5',  '#',  'F', '0', 'fmis:data:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 销售管理 SQL start
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商管理', '0', '30', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='供应商管理' and parent_id=0 and order_num='30';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品供应商', @parentIdFP, '10', '/fmis/suppliers', 'C', '0', 'fmis:suppliers:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '供应商菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商查询', @parentId, '1',  '#',  'F', '0', 'fmis:suppliers:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商新增', @parentId, '2',  '#',  'F', '0', 'fmis:suppliers:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商修改', @parentId, '3',  '#',  'F', '0', 'fmis:suppliers:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商删除', @parentId, '4',  '#',  'F', '0', 'fmis:suppliers:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商导出', @parentId, '5',  '#',  'F', '0', 'fmis:suppliers:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


update biz_product p set  p.model=REPLACE(p.model,concat('-',substring_index(p.model,'-',-1)),'')
where length(p.model) > 5;


update biz_product set procurement_price=price where price>0;


update biz_product p set p.cost_price=ROUND(p.procurement_price*1.08),p.price=ROUND(ROUND(p.procurement_price*1.08)*1.3*1.1)
where p.procurement_price>0;

alter table  biz_quotation modify column freight varchar(100);


alter table biz_quotation add column string11 varchar(100);
alter table biz_quotation add column string12 varchar(100);
alter table biz_quotation add column string13 varchar(100);
alter table biz_quotation add column string14 varchar(100);
alter table biz_quotation add column string15 varchar(100);
alter table biz_quotation add column string16 varchar(100);
alter table biz_quotation add column string17 varchar(100);
alter table biz_quotation add column string18 varchar(100);
alter table biz_quotation add column string19 varchar(100);
alter table biz_quotation add column string20 varchar(100);



drop table if exists biz_product_attachment;
create table biz_product_attachment (
  attachment_id           bigint(20)      not null auto_increment    comment 'ID',
  type           varchar(5)    comment '类型 1=定位器 2=电磁阀，3=回信器数，4=气源三连件，5=可离合减速器',
  bh         varchar(100)          default ''                  comment '商品编号',
  chinese_name         varchar(100)          default ''                  comment '中文品名',
  chinese_specifications       varchar(100)          default ''                  comment '中文规格',
  english_name         varchar(100)          default ''                  comment '英文品名',
  english_specifications         varchar(100)          default ''                  comment '英文规格',
  chinese_packaging         varchar(100)          default ''                  comment '中文包装',
  english_packaging         varchar(100)          default ''                  comment '英文包装',
  chinese_unit         varchar(100)          default ''                  comment '中文单位',
  english_unit         varchar(100)          default ''                  comment '英文单位',
  pressure         varchar(100)          default ''                  comment '压力',
  material         varchar(100)          default ''                  comment '材质',
  bar_code         varchar(100)          default ''                  comment '条形码',
  customs_bh         varchar(100)          default ''                  comment '海关编码',
  handling_fee         varchar(100)          default ''                  comment '操作费',
  color         varchar(100)          default ''                  comment '颜色',
  developer         varchar(100)          default ''                  comment '开发人员',
  goods_category         varchar(100)          default ''                  comment '商品分类',
  supplier         varchar(100)          default ''                  comment '供应商ID',
  box         varchar(100)          default ''                  comment '箱率',
  innerbox_count         varchar(100)          default ''                  comment '内盒数量',
  bulk         varchar(100)          default ''                  comment '体积',
  l         varchar(100)          default ''                  comment '长',
  w         varchar(100)          default ''                  comment '宽',
  h         varchar(100)          default ''                  comment '高',
  gross_weight         varchar(100)          default ''                  comment '毛重',
  net_weight         varchar(100)          default ''                  comment '净重',
  cost_price         decimal(19,5)          default 0                  comment '成本价',
  settlement_price         decimal(19,5)          default 0                  comment '结算价',
  base_price         decimal(19,5)          default 0                  comment '销售底价',
  price         decimal(19,5)          default 0                  comment '销售价',
  string1         varchar(30)          default ''                  comment '备用1',
  string2         varchar(30)          default ''                  comment '备用2',
  string3         varchar(30)          default ''                  comment '备用3',
  string4         varchar(30)          default ''                  comment '备用4',
  string5         varchar(30)          default ''                  comment '备用5',
  string6         varchar(30)          default ''                  comment '备用6',
  string7         varchar(30)          default ''                  comment '备用7',
  string8         varchar(30)          default ''                  comment '备用8',
  string9         varchar(30)          default ''                  comment '备用9',
  string10         varchar(30)          default ''                  comment '备用10',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (attachment_id)
) engine=innodb auto_increment=200 comment = '产品附属表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器', '3', '1', '/fmis/pattachment', 'C', '0', 'fmis:pattachment:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '定位器菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('电磁阀', '3', '1', '/fmis/pattachment1', 'C', '0', 'fmis:pattachment1:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment1:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment1:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment1:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment1:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment1:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- 11111111111
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('回信器数', '3', '1', '/fmis/pattachment2', 'C', '0', 'fmis:pattachment2:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment2:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment2:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment2:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment2:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment2:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('气源三连件', '3', '1', '/fmis/pattachment3', 'C', '0', 'fmis:pattachment3:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment3:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment3:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment3:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment3:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment3:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('可离合减速器', '3', '1', '/fmis/pattachment4', 'C', '0', 'fmis:pattachment4:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment4:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment4:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment4:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment4:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment4:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 定位器
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器', @parentIdF, '70', '/fmis/pattachment', 'C', '0', 'fmis:pattachment:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('定位器导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('电磁阀', @parentIdF, '80', '/fmis/pattachment1', 'C', '0', 'fmis:pattachment1:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment1:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment1:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment1:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment1:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment1:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


--
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('回信器数', @parentIdF, '90', '/fmis/pattachment2', 'C', '0', 'fmis:pattachment2:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment2:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment2:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment2:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment2:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment2:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

--
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('气源三连件', @parentIdF, '100', '/fmis/pattachment3', 'C', '0', 'fmis:pattachment3:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment3:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment3:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment3:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment3:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment3:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- s1
SELECT @parentIdF :=  menu_id from sys_menu where menu_name='产品管理' and parent_id=0;

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('可离合减速器', @parentIdF, '110', '/fmis/pattachment4', 'C', '0', 'fmis:pattachment4:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:pattachment4:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:pattachment4:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:pattachment4:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:pattachment4:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:pattachment4:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




alter table biz_quotation_product add column pattachment_id bigint(20);
alter table biz_quotation_product add column pattachment_count decimal(19,5);
alter table biz_quotation_product add column pattachment_price decimal(19,5);
alter table biz_quotation_product add column pattachment_coefficient decimal(19,5);

alter table biz_quotation_product add column pattachment1_id bigint(20);
alter table biz_quotation_product add column pattachment1_count decimal(19,5);
alter table biz_quotation_product add column pattachment1_price decimal(19,5);
alter table biz_quotation_product add column pattachment1_coefficient decimal(19,5);

alter table biz_quotation_product add column pattachment2_id bigint(20);
alter table biz_quotation_product add column pattachment2_count decimal(19,5);
alter table biz_quotation_product add column pattachment2_price decimal(19,5);
alter table biz_quotation_product add column pattachment2_coefficient decimal(19,5);

alter table biz_quotation_product add column pattachment3_id bigint(20);
alter table biz_quotation_product add column pattachment3_count decimal(19,5);
alter table biz_quotation_product add column pattachment3_price decimal(19,5);
alter table biz_quotation_product add column pattachment3_coefficient decimal(19,5);

alter table biz_quotation_product add column pattachment4_id bigint(20);
alter table biz_quotation_product add column pattachment4_count decimal(19,5);
alter table biz_quotation_product add column pattachment4_price decimal(19,5);
alter table biz_quotation_product add column pattachment4_coefficient decimal(19,5);


alter table biz_quotation_product add column string6 varchar(100);
alter table biz_quotation_product add column string7 varchar(100);
alter table biz_quotation_product add column string8 varchar(100);
alter table biz_quotation_product add column string9 varchar(100);
alter table biz_quotation_product add column string10 varchar(100);
alter table biz_quotation_product add column string11 varchar(100);
alter table biz_quotation_product add column string12 varchar(100);
alter table biz_quotation_product add column string13 varchar(100);
alter table biz_quotation_product add column string14 varchar(100);
alter table biz_quotation_product add column string15 varchar(100);




alter table biz_process_child add column pattachment_id bigint(20);
alter table biz_process_child add column pattachment_count decimal(19,5);
alter table biz_process_child add column pattachment_price decimal(19,5);
alter table biz_process_child add column pattachment_coefficient decimal(19,5);

alter table biz_process_child add column pattachment1_id bigint(20);
alter table biz_process_child add column pattachment1_count decimal(19,5);
alter table biz_process_child add column pattachment1_price decimal(19,5);
alter table biz_process_child add column pattachment1_coefficient decimal(19,5);

alter table biz_process_child add column pattachment2_id bigint(20);
alter table biz_process_child add column pattachment2_count decimal(19,5);
alter table biz_process_child add column pattachment2_price decimal(19,5);
alter table biz_process_child add column pattachment2_coefficient decimal(19,5);

alter table biz_process_child add column pattachment3_id bigint(20);
alter table biz_process_child add column pattachment3_count decimal(19,5);
alter table biz_process_child add column pattachment3_price decimal(19,5);
alter table biz_process_child add column pattachment3_coefficient decimal(19,5);

alter table biz_process_child add column pattachment4_id bigint(20);
alter table biz_process_child add column pattachment4_count decimal(19,5);
alter table biz_process_child add column pattachment4_price decimal(19,5);
alter table biz_process_child add column pattachment4_coefficient decimal(19,5);



alter table biz_customer add column string11 varchar(100);
alter table biz_customer add column string12 varchar(100);
alter table biz_customer add column string13 varchar(100);
alter table biz_customer add column string14 varchar(100);
alter table biz_customer add column string15 varchar(100);
alter table biz_customer add column string16 varchar(100);
alter table biz_customer add column string17 varchar(100);
alter table biz_customer add column string18 varchar(100);
alter table biz_customer add column string19 varchar(100);
alter table biz_customer add column string20 varchar(100);




-- 采购管理 SQL start
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('采购管理', '0', '30', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='采购管理' and parent_id=0 and order_num='30';


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('整机采购', @parentIdFP, '20', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='整机采购' and menu_type='M' and order_num='20';

-- 采购池
-- 菜单 SQL

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('采购池', @parentIdF, '10', '/fmis/procurementpool', 'C', '0', 'fmis:procurementpool:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:procurementpool:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF1 :=  menu_id from sys_menu where menu_name='采购池' and menu_type='C' and order_num='10';

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发起采购', @parentIdF1, '1',  '#',  'F', '0', 'fmis:procurementpool:add',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


SELECT @parentIdF :=  menu_id from sys_menu where menu_name='整机采购' and menu_type='M' and order_num='20';

-- 采购管理
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('采购管理', @parentIdF , '20', '/fmis/procurement', 'C', '0', 'fmis:procurement:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:procurement:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务新增', @parentId, '2',  '#',  'F', '0', 'fmis:procurement:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务修改', @parentId, '3',  '#',  'F', '0', 'fmis:procurement:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务删除', @parentId, '4',  '#',  'F', '0', 'fmis:procurement:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务导出', @parentId, '5',  '#',  'F', '0', 'fmis:procurement:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




drop table if exists biz_data_status;
create table biz_data_status (
  status_id           bigint(20)      not null auto_increment    comment 'ID',
  type           varchar(5)    comment '1=产品 2=执行器 3=法兰 4=螺栓 5=定位器 6=电磁阀 7=回信器数 8=气源三连件 9=可离合减速器',
  child_id bigint(20) comment '关联ID 关联 child表 将来也可以关联dataId',
  data_status varchar(1) comment '例如合同状态 0=未处理 1=已处理',
  product_status varchar(1) comment '产品 0=未处理 1=已处理',
  actuator_status varchar(1) comment '执行器 0=未处理 1=已处理',
  ref1_status varchar(1) comment '法兰 0=未处理 1=已处理',
  ref2_status varchar(1) comment '螺栓 0=未处理 1=已处理',
  p_status varchar(1) comment '定位器 0=未处理 1=已处理',
  p1_status varchar(1) comment '电磁阀 0=未处理 1=已处理',
  p2_status varchar(1) comment '回信器数 0=未处理 1=已处理',
  p3_status varchar(1) comment '气源三连件 0=未处理 1=已处理',
  p4_status varchar(1) comment '可离合减速器 0=未处理 1=已处理',
  string1         varchar(30)          default ''                  comment '备用1',
  string2         varchar(30)          default ''                  comment '备用2',
  string3         varchar(30)          default ''                  comment '备用3',
  string4         varchar(30)          default ''                  comment '备用4',
  string5         varchar(30)          default ''                  comment '备用5',
  string6         varchar(30)          default ''                  comment '备用6',
  string7         varchar(30)          default ''                  comment '备用7',
  string8         varchar(30)          default ''                  comment '备用8',
  string9         varchar(30)          default ''                  comment '备用9',
  string10         varchar(30)          default ''                  comment '备用10',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (status_id)
) engine=innodb auto_increment=200 comment = '数据状态表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态', '3', '1', '/fmis/status', 'C', '0', 'fmis:status:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '数据状态菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态查询', @parentId, '1',  '#',  'F', '0', 'fmis:status:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态新增', @parentId, '2',  '#',  'F', '0', 'fmis:status:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态修改', @parentId, '3',  '#',  'F', '0', 'fmis:status:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态删除', @parentId, '4',  '#',  'F', '0', 'fmis:status:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('数据状态导出', @parentId, '5',  '#',  'F', '0', 'fmis:status:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

update biz_actuator a join biz_suppliers s on s.nick_name=a.string4
set a.string10=s.suppliers_id;

update biz_actuator a join biz_suppliers s on s.nick_name=a.string8 and a.string2='2'
set a.string10=s.suppliers_id;



-- 质量管理 SQL start
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质量管理', '0', '50', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='质量管理' and parent_id=0 and order_num='50';


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('质量检测', @parentIdFP, '10', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='质量检测' and menu_type='M' and order_num='10';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('整机检验', @parentIdF, '10', '/fmis/procurementtest', 'C', '0', 'fmis:procurementtest:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');




drop table if exists biz_data_stestn;
create table biz_data_stestn (
  test_id           bigint(20)      not null auto_increment    comment 'ID',
  status_id bigint(20) comment '状态表id',
  num         decimal(10,5)          default 0               comment '数量',
  string1         varchar(30)          default ''                  comment '备用1',
  string2         varchar(30)          default ''                  comment '备用2',
  string3         varchar(30)          default ''                  comment '备用3',
  string4         varchar(30)          default ''                  comment '备用4',
  string5         varchar(30)          default ''                  comment '备用5',
  string6         varchar(30)          default ''                  comment '备用6',
  string7         varchar(30)          default ''                  comment '备用7',
  string8         varchar(30)          default ''                  comment '备用8',
  string9         varchar(30)          default ''                  comment '备用9',
  string10         varchar(30)          default ''                  comment '备用10',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (test_id)
) engine=innodb auto_increment=200 comment = '采购检测数量';


drop table if exists biz_bill;
create table biz_bill (
  bill_id           bigint(20)      not null auto_increment    comment 'ID',
  xh         varchar(50)          default ''                  comment '序号',
  d         datetime                    comment '日期',
  certificate_number         varchar(30)          default ''                  comment '凭证号',
  settlement_type         varchar(50)          default ''                  comment '结算类别',
  clearing_banks         varchar(30)          default ''                  comment '结算票号',
  pre_month_money         decimal(10,5)          default 0                  comment '上月结转',
  collection_money         decimal(10,5)          default 0                  comment '收款',
  collection_type         varchar(30)          default ''                  comment '收款类别',
  payment         decimal(10,5)          default 0                  comment '付款',
  payment_type         varchar(30)          default ''                  comment '付款类别',
  balance         decimal(10,5)          default 0                  comment '余额',
  type         varchar(3)          default ''                  comment '类型 1=现金日记账 2=银行日记账',
  dept_id         varchar(30)          default ''                  comment '部门ID',
  string1         varchar(30)          default ''                  comment '备用1',
  string2         varchar(30)          default ''                  comment '备用2',
  string3         varchar(30)          default ''                  comment '备用3',
  string4         varchar(30)          default ''                  comment '备用4',
  string5         varchar(30)          default ''                  comment '备用5',
  string6         varchar(30)          default ''                  comment '备用6',
  string7         varchar(30)          default ''                  comment '备用7',
  string8         varchar(30)          default ''                  comment '备用8',
  string9         varchar(30)          default ''                  comment '备用9',
  string10         varchar(30)          default ''                  comment '备用10',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (bill_id)
) engine=innodb auto_increment=200 comment = '日记账表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账', '3', '1', '/fmis/bill', 'C', '0', 'fmis:bill:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '现金日记账菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账查询', @parentId, '1',  '#',  'F', '0', 'fmis:bill:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账新增', @parentId, '2',  '#',  'F', '0', 'fmis:bill:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账修改', @parentId, '3',  '#',  'F', '0', 'fmis:bill:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账删除', @parentId, '4',  '#',  'F', '0', 'fmis:bill:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账导出', @parentId, '5',  '#',  'F', '0', 'fmis:bill:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 财务管理 SQL start
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('财务管理', '0', '60', '#', 'M', '0', '', 'fa fa-folder', 'admin', '2020-06-08', 'admin', '2020-06-08', '');

SELECT @parentIdFP :=  menu_id from sys_menu where menu_name='财务管理' and parent_id=0 and order_num='60';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('资金管理', @parentIdFP, '60', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF :=  menu_id from sys_menu where menu_name='资金管理' and menu_type='M' and order_num='60';

insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('出纳报表', @parentIdF, '10', '#', 'M', '0', '', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

SELECT @parentIdF1 :=  menu_id from sys_menu where menu_name='出纳报表' and menu_type='M' and order_num='10';



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账', @parentIdF1, '10', '/fmis/bill', 'C', '0', 'fmis:bill:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '现金日记账菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账查询', @parentId, '1',  '#',  'F', '0', 'fmis:bill:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账新增', @parentId, '2',  '#',  'F', '0', 'fmis:bill:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账修改', @parentId, '3',  '#',  'F', '0', 'fmis:bill:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账删除', @parentId, '4',  '#',  'F', '0', 'fmis:bill:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('现金日记账导出', @parentId, '5',  '#',  'F', '0', 'fmis:bill:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('银行存款日记', @parentIdF1, '20', '/fmis/bill2', 'C', '0', 'fmis:bill2:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '现金日记账菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:bill2:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:bill2:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:bill2:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:bill2:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:bill2:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


