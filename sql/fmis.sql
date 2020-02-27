
drop table if exists biz_product;
create table biz_product (
  product_id           bigint(20)      not null auto_increment    comment '产品id',
  name         varchar(50)     default ''                 comment '产品名称',
  series         varchar(30)     default ''                 comment '系列 数据字典',
  model         varchar(30)          default 0                  comment '产品型号',
  specifications         varchar(30)          default 0                  comment '规格',
  nominal_pressure         varchar(30)          default 0                  comment '公称压力',
  connection_type         varchar(30)          default 0                  comment '连接方式',
  valvebody_material         varchar(30)          default 0                  comment '阀体材质',
  valve_material         varchar(30)          default 0                  comment '阀板材质',
  sealing_material         varchar(30)          default 0                  comment '密封材质',
  valve_element         varchar(30)          default 0                  comment '阀芯材质',
  drive_form         varchar(30)          default 0                  comment '驱动形式',
  price         decimal(10,5)          default 0                  comment '产品单价',
  supplier         varchar(30)          default 0                  comment '供应商名称',
  remark         varchar(30)          default 0                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (product_id)
) engine=innodb auto_increment=200 comment = '业务产品表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品', '3', '1', '/fmis/product', 'C', '0', 'fmis:product:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务产品菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品查询', @parentId, '1',  '#',  'F', '0', 'fmis:product:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品新增', @parentId, '2',  '#',  'F', '0', 'fmis:product:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品修改', @parentId, '3',  '#',  'F', '0', 'fmis:product:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品删除', @parentId, '4',  '#',  'F', '0', 'fmis:product:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务产品导出', @parentId, '5',  '#',  'F', '0', 'fmis:product:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');



--产品名称	系列	产品型号（ID）	规格	公称压力	连接方式	阀体材质	阀板材质	密封材质	阀芯材质	驱动形式	产品单价（元）	供应商名称（代号）	备注