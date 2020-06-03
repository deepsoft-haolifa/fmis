
alter table  biz_flow modify column flow_status varchar(10);



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

drop table if exists biz_area_dict;
CREATE TABLE biz_area_dict
(
     areas 	varchar(50),
     code_name 	varchar(50),
     area varchar(50)
);

INSERT INTO biz_area_dict ( areas , code_name , area )
VALUES
    ('华北', 'X', '北京'),
    ('华北', 'Y', '天津'),
    ('华北', 'A', '山西'),
    ('华北', 'H', '河北'),
    ('华北', 'M', '内蒙古'),
    ('西北', 'B', '陕西'),
    ('西北', 'W', '宁夏'),
    ('西北', 'W', '甘肃'),
    ('西北', 'W', '新疆'),
    ('西北', 'W', '青海'),
    ('西南', 'D', '四川'),
    ('西南', 'D', '重庆'),
    ('西南', '', '西藏'),
    ('西南', 'N', '云南'),
    ('华中', 'F', '湖南'),
    ('华中', 'F', '湖北'),
    ('华中', 'T', '河南'),
    ('华中', 'E', '江西'),
    ('华南', 'G', '广东'),
    ('华南', 'G', '海南'),
    ('华南', 'G', '广西'),
    ('华东', 'F', '福建'),
    ('华东', 'S', '山东'),
    ('华东', 'S', '贵州'),
    ('华东', 'Z', '上海'),
    ('华东', 'J', '浙江'),
    ('华东', 'J', '江苏'),
    ('华东', 'J', '安徽'),
    ('东北', 'L', '黑龙江'),
    ('东北', 'L', '吉林'),
    ('东北', 'L', '辽宁');

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




-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典', '3', '1', '/fmis/careadict', 'C', '0', 'fmis:careadict:view', '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '客户区域字典菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典查询', @parentId, '1',  '#',  'F', '0', 'fmis:careadict:list',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典新增', @parentId, '2',  '#',  'F', '0', 'fmis:careadict:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典修改', @parentId, '3',  '#',  'F', '0', 'fmis:careadict:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典删除', @parentId, '4',  '#',  'F', '0', 'fmis:careadict:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户区域字典导出', @parentId, '5',  '#',  'F', '0', 'fmis:careadict:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');


alter table biz_customer add column string1 varchar(50);
alter table biz_customer modify column string1 varchar(50) comment '备用string1';

alter table biz_customer add column string2 varchar(50);
alter table biz_customer modify column string2 varchar(50) comment '备用string2';

alter table biz_customer add column string3 varchar(50);
alter table biz_customer modify column string3 varchar(50) comment '备用string3';

alter table biz_customer add column string4 varchar(50);
alter table biz_customer modify column string4 varchar(50) comment '备用string4';

alter table biz_customer add column string5 varchar(50);
alter table biz_customer modify column string5 varchar(50) comment '备用string5';

alter table biz_customer add column string6 varchar(50);
alter table biz_customer modify column string6 varchar(50) comment '备用string6';

alter table biz_customer add column string7 varchar(50);
alter table biz_customer modify column string7 varchar(50) comment '备用string7';

alter table biz_customer add column string8 varchar(50);
alter table biz_customer modify column string8 varchar(50) comment '备用string8';

alter table biz_customer add column string9 varchar(50);
alter table biz_customer modify column string9 varchar(50) comment '备用string9';

alter table biz_customer add column string10 varchar(50);
alter table biz_customer modify column string10 varchar(50) comment '备用string10';
