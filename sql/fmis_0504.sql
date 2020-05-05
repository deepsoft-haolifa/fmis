--销售员编制-主管审批-总经理审批-销售执行。
--销售发起－副总5万－总经理10万
--商务发起－商务经理－财务账务（预付、货到）－总经理（10万内含）－（条件外）股东批－物流
--销售内勤－商务经理－财务总－开票员（开票及反馈），缺反馈表
--流程和角色定义



drop table if exists biz_process_define;
create table biz_process_define (
  define_id           bigint(20)      not null auto_increment    comment 'ID',
  tb_name         varchar(20)      default 0                 comment '业余表名称',
  define_flow		varchar(50)         default '0' 	comment '定义状态 1-2-3-4-5',
  define_role		varchar(300)         default '0' 	comment '定义角色 process_ywy-process_xsjl-process_qyjl-process_fz-process_lz',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (define_id)
) engine=innodb auto_increment=200 comment = '业务流程定义';


drop table if exists biz_process_data;
create table biz_process_data (
  data_id           bigint(20)      not null auto_increment    comment 'ID',
  biz_data_id         bigint(20)      default 0                 comment '关联业务数据ID',
  biz_id         varchar(20)      default 0                 comment '业务ID 用户区分哪个业务，contract=合同 procurement=采购 delivery=发货 invoice=开票',
  normal_flag char(1)         default '0'                comment '审批节点 到哪个节点结束',
  flow_status		varchar(2)         default '0' 	comment '流程状态-2=未上报 0=上报',
  remark         varchar(30)          default ''                  comment '备注',

  price1         decimal(10,5)          default 0               comment '价格1',
  price2         decimal(10,5)          default 0               comment '价格2',
  price3         decimal(10,5)          default 0               comment '价格3',
  price4         decimal(10,5)          default 0               comment '价格4',
  price5         decimal(10,5)          default 0               comment '价格5',
  price6         decimal(10,5)          default 0               comment '价格6',
  price7         decimal(10,5)          default 0               comment '价格7',
  price8         decimal(10,5)          default 0               comment '价格8',
  price9         decimal(10,5)          default 0               comment '价格9',
  price10         decimal(10,5)          default 0               comment '价格10',



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
  string11         varchar(30)          default ''                  comment '备用11',
  string12         varchar(30)          default ''                  comment '备用12',
  string13         varchar(30)          default ''                  comment '备用13',
  string14         varchar(30)          default ''                  comment '备用14',
  string15         varchar(30)          default ''                  comment '备用15',
  string16         varchar(30)          default ''                  comment '备用16',
  string17         varchar(30)          default ''                  comment '备用17',
  string18         varchar(30)          default ''                  comment '备用18',
  string19         varchar(30)          default ''                  comment '备用19',
  string20         varchar(30)          default ''                  comment '备用20',

  datetime1 datetime comment '时间1',
  datetime2 datetime comment '时间2',
  datetime3 datetime comment '时间3',
  datetime4 datetime comment '时间4',
  datetime5 datetime comment '时间5',

  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (data_id)
) engine=innodb auto_increment=200 comment = '流程业务数据表';


drop table if exists biz_process_child;
create table biz_process_child (
  child_id           bigint(20)      not null auto_increment    comment 'ID',
  data_id         bigint(20)     default 0                 comment '数据ID',


  remark         varchar(30)          default ''                  comment '备注',



  price1         decimal(10,5)          default 0               comment '价格1',
  price2         decimal(10,5)          default 0               comment '价格2',
  price3         decimal(10,5)          default 0               comment '价格3',
  price4         decimal(10,5)          default 0               comment '价格4',
  price5         decimal(10,5)          default 0               comment '价格5',
  price6         decimal(10,5)          default 0               comment '价格6',
  price7         decimal(10,5)          default 0               comment '价格7',
  price8         decimal(10,5)          default 0               comment '价格8',
  price9         decimal(10,5)          default 0               comment '价格9',
  price10         decimal(10,5)          default 0               comment '价格10',



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
  string11         varchar(30)          default ''                  comment '备用11',
  string12         varchar(30)          default ''                  comment '备用12',
  string13         varchar(30)          default ''                  comment '备用13',
  string14         varchar(30)          default ''                  comment '备用14',
  string15         varchar(30)          default ''                  comment '备用15',
  string16         varchar(30)          default ''                  comment '备用16',
  string17         varchar(30)          default ''                  comment '备用17',
  string18         varchar(30)          default ''                  comment '备用18',
  string19         varchar(30)          default ''                  comment '备用19',
  string20         varchar(30)          default ''                  comment '备用20',

  datetime1 datetime comment '时间1',
  datetime2 datetime comment '时间2',
  datetime3 datetime comment '时间3',
  datetime4 datetime comment '时间4',
  datetime5 datetime comment '时间5',

  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (child_id)
) engine=innodb auto_increment=200 comment = '流程数据字表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字', '3', '1', '/fmis/child', 'C', '0', 'fmis:child:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '流程数据字菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字查询', @parentId, '1',  '#',  'F', '0', 'fmis:child:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字新增', @parentId, '2',  '#',  'F', '0', 'fmis:child:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字修改', @parentId, '3',  '#',  'F', '0', 'fmis:child:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字删除', @parentId, '4',  '#',  'F', '0', 'fmis:child:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('流程数据字导出', @parentId, '5',  '#',  'F', '0', 'fmis:child:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理', '3', '1', '/fmis/data', 'C', '0', 'fmis:data:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '合同管理菜单');

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


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义', '3', '1', '/fmis/define', 'C', '0', 'fmis:define:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务流程定义菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义查询', @parentId, '1',  '#',  'F', '0', 'fmis:define:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义新增', @parentId, '2',  '#',  'F', '0', 'fmis:define:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义修改', @parentId, '3',  '#',  'F', '0', 'fmis:define:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义删除', @parentId, '4',  '#',  'F', '0', 'fmis:define:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务流程定义导出', @parentId, '5',  '#',  'F', '0', 'fmis:define:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

----合同管理
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('合同管理', '2148', '1', '/fmis/data', 'C', '0', 'fmis:data:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:data:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务新增', @parentId, '2',  '#',  'F', '0', 'fmis:data:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务修改', @parentId, '3',  '#',  'F', '0', 'fmis:data:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务删除', @parentId, '4',  '#',  'F', '0', 'fmis:data:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务导出', @parentId, '5',  '#',  'F', '0', 'fmis:data:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');




----发货管理
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('发货管理', '2148', '1', '/fmis/delivery', 'C', '0', 'fmis:delivery:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:delivery:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务新增', @parentId, '2',  '#',  'F', '0', 'fmis:delivery:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务修改', @parentId, '3',  '#',  'F', '0', 'fmis:delivery:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务删除', @parentId, '4',  '#',  'F', '0', 'fmis:delivery:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务导出', @parentId, '5',  '#',  'F', '0', 'fmis:delivery:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


