drop table if exists biz_dict;
create table biz_dict (
  dict_id           bigint(20)      not null auto_increment    comment 'ID',
  name         varchar(50)     default ''                 comment '名称',
  name1         varchar(50)     default ''                 comment '名称1',
  code         varchar(200)     default ''                 comment 'Code',
  parent_id         bigint(20)    default 0                comment '上级ID',
  parent_type         varchar(200)     default ''                comment '上级type',
  remark         varchar(30)          default ''                  comment '备注',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dict_id)
) engine=innodb auto_increment=200 comment = '业务数据字典';


insert into biz_dict(dict_id,name,code,parent_id,status,del_flag)
values(1,'产品型号','product_type',-1,0,0);

insert into biz_dict(dict_id,name,code,parent_id,status,del_flag)
values
(2,'弹簧载荷安全阀','A',1,0,0),
(3,'蝶阀','D',1,0,0),
(4,'隔膜阀','G',1,0,0),
(5,'止回阀','H',1,0,0),
(6,'截止阀','J',1,0,0),
(7,'节流阀','L',1,0,0),
(8,'柱塞阀','U',1,0,0),
(9,'闸阀','Z',1,0,0),
(10,'球阀','Q',1,0,0),
(11,'减压阀','Y',1,0,0),
(12,'过滤器','GL',1,0,0),
(13,'管道连接件','GD',1,0,0),
(14,'水利控制阀','XK',1,0,0),
(15,'排气阀','PQ',1,0,0),
(16,'调节阀','TJ',1,0,0),
(17,'蒸汽疏水阀','S',1,0,0),
(18,'平衡阀','PH',1,0,0);


insert into biz_dict(dict_id,name,code,parent_id,status,del_flag)
values(30,'驱动方式','drive_mode',-1,0,0);

insert into biz_dict(dict_id,name,code,parent_id,status,del_flag)
values
	(31,'连接形式','link_type',-1,0,0),
	(32,'结构形式','structural_type',-1,0,0),
	(33,'密封材质','sealing_material',-1,0,0),
	(34,'阀芯材质','spool_material',-1,0,0),
	(35,'公称压力','nominal_pressure',-1,0,0),
	(36,'阀体材质','body_material',-1,0,0),
	(37,'规格','specification',-1,0,0),
	(38,'品质等级','quality_level',-1,0,0);



insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,2,0,0,'quality_level' from temp_a where 品质等级 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,2,0,0,'specification' from temp_a where 规格 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,2,0,0,'body_material' from temp_a where 阀体材质 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,2,0,0,'nominal_pressure' from temp_a where 公称压力 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,2,0,0,'spool_material' from temp_a where 阀芯材质 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,2,0,0,'sealing_material' from temp_a where 密封材质 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,2,0,0,'drive_mode' from temp_a where 驱动方式 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,2,0,0,'link_type' from temp_a where 连接形式 != ''

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,2,0,0,'structural_type' from temp_a where 结构形式 != ''


---temp_d


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,3,0,0,'quality_level' from temp_d where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,3,0,0,'specification' from temp_d where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,3,0,0,'body_material' from temp_d where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,3,0,0,'nominal_pressure' from temp_d where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,3,0,0,'spool_material' from temp_d where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,3,0,0,'sealing_material' from temp_d where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,3,0,0,'drive_mode' from temp_d where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,3,0,0,'link_type' from temp_d where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,3,0,0,'structural_type' from temp_d where 结构形式 != '';



---temp_g


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,4,0,0,'quality_level' from temp_g where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,4,0,0,'specification' from temp_g where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,4,0,0,'body_material' from temp_g where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,4,0,0,'nominal_pressure' from temp_g where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,4,0,0,'spool_material' from temp_g where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封形式,密封形式Code,4,0,0,'sealing_material' from temp_g where 密封形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,4,0,0,'drive_mode' from temp_g where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,4,0,0,'link_type' from temp_g where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,4,0,0,'structural_type' from temp_g where 结构形式 != '';


---temp_h


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,5,0,0,'quality_level' from temp_h where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,5,0,0,'specification' from temp_h where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,5,0,0,'body_material' from temp_h where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,5,0,0,'nominal_pressure' from temp_h where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,5,0,0,'spool_material' from temp_h where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,5,0,0,'sealing_material' from temp_h where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,5,0,0,'drive_mode' from temp_h where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,5,0,0,'link_type' from temp_h where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,5,0,0,'structural_type' from temp_h where 结构形式 != '';


---temp_j


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,6,0,0,'quality_level' from temp_j where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,6,0,0,'specification' from temp_j where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,6,0,0,'body_material' from temp_j where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,6,0,0,'nominal_pressure' from temp_j where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,6,0,0,'spool_material' from temp_j where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,6,0,0,'sealing_material' from temp_j where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,6,0,0,'drive_mode' from temp_j where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,6,0,0,'link_type' from temp_j where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,6,0,0,'structural_type' from temp_j where 结构形式 != '';


---temp_l


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,7,0,0,'quality_level' from temp_l where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,7,0,0,'specification' from temp_l where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,7,0,0,'body_material' from temp_l where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,7,0,0,'nominal_pressure' from temp_l where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,7,0,0,'spool_material' from temp_l where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,7,0,0,'sealing_material' from temp_l where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,7,0,0,'drive_mode' from temp_l where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,7,0,0,'link_type' from temp_l where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,7,0,0,'structural_type' from temp_l where 结构形式 != '';




---temp_u


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,8,0,0,'quality_level' from temp_u where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,8,0,0,'specification' from temp_u where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,8,0,0,'body_material' from temp_u where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,8,0,0,'nominal_pressure' from temp_u where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,8,0,0,'spool_material' from temp_u where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,8,0,0,'sealing_material' from temp_u where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,8,0,0,'drive_mode' from temp_u where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,8,0,0,'link_type' from temp_u where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,8,0,0,'structural_type' from temp_u where 结构形式 != '';



---temp_z


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,9,0,0,'quality_level' from temp_z where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,9,0,0,'specification' from temp_z where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,9,0,0,'body_material' from temp_z where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,9,0,0,'nominal_pressure' from temp_z where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,9,0,0,'spool_material' from temp_z where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,9,0,0,'sealing_material' from temp_z where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,9,0,0,'drive_mode' from temp_z where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,9,0,0,'link_type' from temp_z where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1,'-',结构形式2,'-',结构形式3),结构形式Code,9,0,0,'structural_type' from temp_z where 结构形式 != '';


---temp_q


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,10,0,0,'quality_level' from temp_q where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,10,0,0,'specification' from temp_q where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,10,0,0,'body_material' from temp_q where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,10,0,0,'nominal_pressure' from temp_q where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,10,0,0,'spool_material' from temp_q where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,10,0,0,'sealing_material' from temp_q where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,10,0,0,'drive_mode' from temp_q where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,10,0,0,'link_type' from temp_q where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,10,0,0,'structural_type' from temp_q where 结构形式 != '';






---temp_y


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,11,0,0,'quality_level' from temp_y where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,11,0,0,'specification' from temp_y where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,11,0,0,'body_material' from temp_y where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,11,0,0,'nominal_pressure' from temp_y where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,11,0,0,'spool_material' from temp_y where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,11,0,0,'sealing_material' from temp_y where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,11,0,0,'drive_mode' from temp_y where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,11,0,0,'link_type' from temp_y where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,11,0,0,'structural_type' from temp_y where 结构形式 != '';





---temp_gl


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,12,0,0,'quality_level' from temp_gl where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,12,0,0,'specification' from temp_gl where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,12,0,0,'body_material' from temp_gl where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,12,0,0,'nominal_pressure' from temp_gl where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,12,0,0,'spool_material' from temp_gl where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封形式,密封形式Code,12,0,0,'sealing_material' from temp_gl where 密封形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,12,0,0,'drive_mode' from temp_gl where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,12,0,0,'link_type' from temp_gl where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,12,0,0,'structural_type' from temp_gl where 结构形式 != '';



---temp_gd


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,13,0,0,'quality_level' from temp_gd where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,13,0,0,'specification' from temp_gd where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,13,0,0,'body_material' from temp_gd where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,13,0,0,'nominal_pressure' from temp_gd where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,13,0,0,'spool_material' from temp_gd where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,13,0,0,'sealing_material' from temp_gd where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,13,0,0,'drive_mode' from temp_gd where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,13,0,0,'link_type' from temp_gd where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,13,0,0,'structural_type' from temp_gd where 结构形式 != '';



---temp_xk


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,14,0,0,'quality_level' from temp_xk where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,14,0,0,'specification' from temp_xk where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,14,0,0,'body_material' from temp_xk where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,14,0,0,'nominal_pressure' from temp_xk where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,14,0,0,'spool_material' from temp_xk where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,14,0,0,'sealing_material' from temp_xk where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,14,0,0,'drive_mode' from temp_xk where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,14,0,0,'link_type' from temp_xk where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select concat(结构形式,'-',结构形式1),结构形式Code,14,0,0,'structural_type' from temp_xk where 结构形式 != '';



---temp_pq


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,15,0,0,'quality_level' from temp_pq where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,15,0,0,'specification' from temp_pq where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,15,0,0,'body_material' from temp_pq where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,15,0,0,'nominal_pressure' from temp_pq where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,15,0,0,'spool_material' from temp_pq where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,15,0,0,'sealing_material' from temp_pq where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,15,0,0,'drive_mode' from temp_pq where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,15,0,0,'link_type' from temp_pq where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,15,0,0,'structural_type' from temp_pq where 结构形式 != '';





---temp_tj


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,16,0,0,'quality_level' from temp_tj where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,16,0,0,'specification' from temp_tj where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,16,0,0,'body_material' from temp_tj where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,16,0,0,'nominal_pressure' from temp_tj where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,16,0,0,'spool_material' from temp_tj where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,16,0,0,'sealing_material' from temp_tj where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,16,0,0,'drive_mode' from temp_tj where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,16,0,0,'link_type' from temp_tj where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,16,0,0,'structural_type' from temp_tj where 结构形式 != '';





---temp_s


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,17,0,0,'quality_level' from temp_s where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,17,0,0,'specification' from temp_s where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,17,0,0,'body_material' from temp_s where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,17,0,0,'nominal_pressure' from temp_s where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,17,0,0,'spool_material' from temp_s where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,17,0,0,'sealing_material' from temp_s where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,17,0,0,'drive_mode' from temp_s where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,17,0,0,'link_type' from temp_s where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,17,0,0,'structural_type' from temp_s where 结构形式 != '';



---temp_ph


insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 品质等级,品质等级,18,0,0,'quality_level' from temp_ph where 品质等级 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 规格,规格,18,0,0,'specification' from temp_ph where 规格 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀体材质,阀体材质Code,18,0,0,'body_material' from temp_ph where 阀体材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 公称压力,公称压力Code,18,0,0,'nominal_pressure' from temp_ph where 公称压力 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 阀芯材质,阀芯材质Code,18,0,0,'spool_material' from temp_ph where 阀芯材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 密封材质,密封材质Code,18,0,0,'sealing_material' from temp_ph where 密封材质 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 驱动方式,驱动方式Code,18,0,0,'drive_mode' from temp_ph where 驱动方式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 连接形式,连接形式Code,18,0,0,'link_type' from temp_ph where 连接形式 != '';

insert into biz_dict(name,code,parent_id,status,del_flag,parent_type)
select 结构形式,结构形式Code,18,0,0,'structural_type' from temp_ph where 结构形式 != '';



