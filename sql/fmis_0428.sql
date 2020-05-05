alter table biz_customer add column customer_level varchar(50);
alter table biz_customer modify column customer_level varchar(50) comment '客户类别';

alter table biz_customer add column code_name varchar(50);
alter table biz_customer modify column code_name varchar(50) comment '客户代号';

alter table biz_customer add column industry_division varchar(50);
alter table biz_customer modify column industry_division varchar(50) comment '行业划分';

alter table biz_customer add column allocation_date datetime;
alter table biz_customer modify column allocation_date datetime comment '分配日期';

alter table biz_customer add column assign_number varchar(50);
alter table biz_customer modify column assign_number varchar(50) comment '分配次数';

alter table biz_customer add column file_number varchar(50);
alter table biz_customer modify column file_number varchar(50) comment '档案编号';

alter table biz_customer add column file_paths varchar(100);
alter table biz_customer modify column file_paths varchar(100) comment '附件地址';

alter table biz_customer add column new_status varchar(2);
alter table biz_customer modify column new_status varchar(2) default '0' comment '新老状态 0=新 1=老';
--成单状态 追踪月份	客户备案时间	商务代码	市场区域	追踪状态	客户名称	客户代号	新/老	累计订货次数	合同待执行	追踪反馈	业务负责人	闭环时间	联系人	电话	传真	E-mail	备注

drop table if exists biz_customer_track;
create table biz_customer_track (
  track_id           bigint(20)      not null auto_increment    comment 'ID',
  customer_id         bigint(20)      default 0                 comment '客户ID',
  single_state         varchar(2)      default '0'                 comment '成单状态 customer_single_state',
  reportprice_state         varchar(2)      default '0'                 comment '报价状态 customer_reportprice_state',
  contract_executed         varchar(50)      default '0'                 comment '合同待执行',
  offer_tracked         varchar(50)      default '0'                 comment '报价待追踪',
  order_number		bigint(20)         default 0 	comment '订货次数',
  feedback         varchar(200)          default ''                  comment '追踪反馈',
  remark         varchar(200)          default ''                  comment '备注',
  email         varchar(30)          default ''                  comment 'email',
  closedloop_time         datetime                            comment '闭环时间',
  contacts         varchar(50)     default ''                 comment '联系人',
  contact_phone         varchar(50)     default ''                 comment '电话',
  fax         varchar(50)     default ''                 comment '传真',
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
  primary key (track_id)
) engine=innodb auto_increment=200 comment = '客户追踪';
ALTER TABLE biz_customer_track COMMENT '客户追踪';
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪', '3', '1', '/fmis/customertrack', 'C', '0', 'fmis:customertrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户追踪菜单');

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


insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户追踪', '2148', '1', '/fmis/customertrack', 'C', '0', 'fmis:customertrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '业务菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:customertrack:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务新增', @parentId, '2',  '#',  'F', '0', 'fmis:customertrack:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务修改', @parentId, '3',  '#',  'F', '0', 'fmis:customertrack:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务删除', @parentId, '4',  '#',  'F', '0', 'fmis:customertrack:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务导出', @parentId, '5',  '#',  'F', '0', 'fmis:customertrack:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

--报价顺序号 溢价比例 追踪反馈	闭环时间	报价备案时间	报价时效	联系人	电话	传真	E-mail
--报价单状态为：已成交/未成交/未反馈/待跟进
drop table if exists biz_quotation_track;
create table biz_quotation_track (
  track_id           bigint(20)      not null auto_increment    comment 'ID',
  quotation_id         bigint(20)      default 0                 comment '报价单ID',
  quotation_state         varchar(2)      default '0'                 comment '报价单状态 quotation_state 已成交/未成交/未反馈/待跟进',
  serial_number         varchar(10)      default '0'                 comment '报价顺序号',
  premium_rate         varchar(10)      default '0'                 comment '溢价比例',
  feedback         varchar(200)          default ''                  comment '追踪反馈',
  closedloop_time         datetime                            comment '闭环时间',
  record_time         datetime                            comment '报价备案时间',
  limitation         varchar(50)      default '0'                 comment '报价时效',
  email         varchar(30)          default ''                  comment 'email',
  contacts         varchar(50)     default ''                 comment '联系人',
  contact_phone         varchar(50)     default ''                 comment '电话',
  fax         varchar(50)     default ''                 comment '传真',
  remark         varchar(200)          default ''                  comment '备注',
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
  primary key (track_id)
) engine=innodb auto_increment=200 comment = '报价追踪';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报价单追踪', '3', '1', '/fmis/quotationtrack', 'C', '0', 'fmis:quotationtrack:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '报价追踪菜单');

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

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务查询', @parentId, '1',  '#',  'F', '0', 'fmis:quotationtrack:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务新增', @parentId, '2',  '#',  'F', '0', 'fmis:quotationtrack:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务修改', @parentId, '3',  '#',  'F', '0', 'fmis:quotationtrack:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务删除', @parentId, '4',  '#',  'F', '0', 'fmis:quotationtrack:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('业务导出', @parentId, '5',  '#',  'F', '0', 'fmis:quotationtrack:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


