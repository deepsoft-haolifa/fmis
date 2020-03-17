
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


alter table biz_product add column structural_style varchar(30);
alter table biz_product modify column structural_style varchar(30) comment '结构形式';
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



drop table if exists biz_customer;
create table biz_customer (
  customer_id           bigint(20)      not null auto_increment    comment '客户ID',
  record_date	datetime comment '备案日期',
  company_code         varchar(50)     default ''                 comment '商务公司代码',
  area         varchar(50)     default ''                 comment '客户所属区域',
  record_code         varchar(50)     default ''                 comment '项目备案号',
  owner_user_id         bigint(20)                 comment '业务负责人',
  name         varchar(50)     default ''                 comment '客户名称',
  project_ame         varchar(50)     default ''                 comment '项目名称',
  contact_name         varchar(50)     default ''                 comment '联系人姓名',
  contact_position         varchar(50)     default ''                 comment '联系人职务',
  contact_phone         varchar(50)     default ''                 comment '联系人电话',
  contact_email         varchar(50)     default ''                 comment '联系人邮箱',
  brand         varchar(50)     default ''                 comment '品牌',
  info         varchar(50)     default ''                 comment '客户/信息',
  product_info         varchar(50)     default ''                 comment '涉及产品',
  remark         varchar(30)          default 0                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (customer_id)
) engine=innodb auto_increment=200 comment = '客户表';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户管理', '3', '1', '/fmis/customer', 'C', '0', 'fmis:customer:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户菜单');

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



drop table if exists biz_actuator_ref;
create table biz_actuator_ref (
  ar_id           bigint(20)      not null auto_increment    comment 'ID',
  brand         varchar(50)     default ''                 comment '品牌名称',
  drive_type         varchar(50)     default ''                 comment '驱动类型',
  media_type         varchar(50)     default ''                 comment '介质类型',
  valve_type         varchar(50)     default ''                 comment '阀门规格 对应产品表规格',
  top_flange         varchar(50)     default ''                 comment '上法兰',
  pressure         varchar(50)     default ''                 comment '压力 对应产品表压力',
  torsion         varchar(50)     default ''                 comment '扭矩',
  multiplying_power         varchar(50)     default ''                 comment '倍率',
  drive_mode         varchar(50)     default ''                 comment '驱动器型号',
  fit_type         varchar(50)     default ''                 comment '适配系列',
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
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  remark         varchar(30)          default 0                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (ar_id)
) engine=innodb auto_increment=200 comment = '执行器关系管理';


drop table if exists biz_actuator;
create table biz_actuator (
  actuator_id           bigint(20)      not null auto_increment    comment 'ID',
  name         varchar(50)     default ''                 comment '产品名称',
  brand         varchar(50)     default ''                 comment '执行器品牌',
  manufacturer         varchar(50)     default ''                 comment '生产厂家',
  setup_type         varchar(50)     default ''                 comment '安装形式',
  output_torque         varchar(50)     default ''                 comment '输出力距',
  action_type         varchar(50)     default ''                 comment '开启时间',
  control_circuit         varchar(50)     default ''                 comment '控制电路',
  adaptable_voltage         varchar(50)     default ''                 comment '适用电压',
  protection_level         varchar(50)     default ''                 comment '防护等级',
  quality_level         varchar(50)     default ''                 comment '品质等级',
  explosion_level         varchar(50)     default ''                 comment '防爆等级',
  price         decimal(10,5) default 0                  comment '价格',
  remark         varchar(30)          default 0                  comment '备注',
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
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (actuator_id)
) engine=innodb auto_increment=200 comment = '执行器';



--Suppliers
drop table if exists biz_suppliers;
create table biz_suppliers (
  suppliers_id           bigint(20)      not null auto_increment    comment 'ID',
  name         varchar(50)     default ''                 comment '公司名称',
  address         varchar(200)     default ''                 comment '地址',
  telphone         varchar(50)     default ''                 comment '电话',
  fax         varchar(50)     default ''                 comment '传真',
  email         varchar(50)     default ''                 comment 'Email',
  site_url         varchar(50)     default ''                 comment '网址',
  owner_id         varchar(50)     default 0                 comment '负责人',
  contact         varchar(50)     default ''                 comment '联系人',
  manage_state         varchar(50)     default ''                 comment '经营形态',
  market_distribution         varchar(50)     default ''                 comment '市场分布',
  target_customer         varchar(50)     default ''                 comment '目标客户',
  human_capital_measure         varchar(50)     default ''                 comment '人力资源状况',
  taxpayer         varchar(50)     default ''                 comment '一般纳税人',
  remark         varchar(30)          default 0                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (suppliers_id)
) engine=innodb auto_increment=200 comment = '供应商';



alter table biz_suppliers add column nick_name varchar(50);
alter table biz_suppliers modify column nick_name varchar(50) comment '代号';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('供应商', '3', '1', '/fmis/suppliers', 'C', '0', 'fmis:suppliers:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '供应商菜单');

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


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务数据字典', '3', '1', '/fmis/dict', 'C', '0', 'fmis:dict:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务数据字典菜单');

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



drop table if exists biz_product_ref;
create table biz_product_ref (
  product_ref_id           bigint(20)      not null auto_increment    comment 'ID',
  name         varchar(50)     default ''                 comment '名称',
  type         char(1)     default ''                 comment '类型 1=法兰 2=螺栓',
  model         varchar(50)     default ''                 comment '型号',
  specifications         varchar(50)     default ''                 comment '规格 对应规格',
  valvebody_material         varchar(30)     default ''                 comment '材质 对应阀体材质',
  material_require         varchar(30)     default ''                comment '材质要求',
  price         decimal(10,5)          default 0               comment '单价',
  remark         varchar(30)          default ''                  comment '备注',
  suppliers_id         bigint(20)          default 0                  comment '供应商',
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
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (product_ref_id)
) engine=innodb auto_increment=200 comment = '产品关系表';



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器关系管理', '3', '1', '/fmis/actuatorRef', 'C', '0', 'fmis:actuatorRef:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '执行器关系管理菜单');

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



-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('产品关系', '3', '1', '/fmis/productRef', 'C', '0', 'fmis:productRef:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '产品关系菜单');

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


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('执行器', '3', '1', '/fmis/actuator', 'C', '0', 'fmis:actuator:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '执行器菜单');

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
