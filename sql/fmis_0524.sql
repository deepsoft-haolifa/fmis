

alter table  biz_customer modify column company_address varchar(500);

alter table  biz_customer modify column contact_phone varchar(100);

alter table  biz_customer modify column telephone varchar(100);


drop table if exists biz_customer_history;
create table biz_customer_history (
  history_id           bigint(20)      not null auto_increment    comment 'ID',
  customer_id           bigint(20)    comment '客户ID',
  old_id         bigint(20)      default 0                 comment '老用户ID',
  new_id         bigint(20)      default 0                 comment '新用户ID',
  string1         varchar(100)          default ''                  comment '备用1',
  string2         varchar(100)          default ''                  comment '备用2',
  string3         varchar(100)          default ''                  comment '备用3',
  string4         varchar(100)          default ''                  comment '备用4',
  string5         varchar(100)          default ''                  comment '备用5',
  string6         varchar(100)          default ''                  comment '备用6',
  string7         varchar(100)          default ''                  comment '备用7',
  string8         varchar(100)          default ''                  comment '备用8',
  string9         varchar(100)          default ''                  comment '备用9',
  string10         varchar(100)          default ''                  comment '备用10',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (history_id)
) engine=innodb auto_increment=200 comment = '客户分配记录表';


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户分配记录', '2148', '1', '/fmis/chistory', 'C', '0', 'fmis:chistory:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户分配记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('查询', @parentId, '1',  '#',  'F', '0', 'fmis:chistory:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('新增', @parentId, '2',  '#',  'F', '0', 'fmis:chistory:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('修改', @parentId, '3',  '#',  'F', '0', 'fmis:chistory:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('删除', @parentId, '4',  '#',  'F', '0', 'fmis:chistory:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('导出', @parentId, '5',  '#',  'F', '0', 'fmis:chistory:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');
