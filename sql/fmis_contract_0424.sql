
drop table if exists biz_contract;
create table biz_contract (
  contract_id           bigint(20)      not null auto_increment    comment 'ID',
  product_id         bigint(20)      default 0                 comment '产品ID',
  product_ref1_id         bigint(20)      default 0                  comment '产品配件法兰id',
  product_ref1_num         varchar(30)      default 0                  comment '产品配件法兰数量',
  product_ref2_id         bigint(20)      default 0                  comment '产品配件螺栓id',
  product_ref2_num         varchar(30)      default 0                  comment '产品配件螺栓数量',
  actuator_id	bigint(20)      default 0                  comment '执行器id',
  special_expenses         varchar(50)     default ''                 comment '特殊费用',
  payment_method         varchar(50)     default ''                 comment '付款方式',
  freight         decimal(10,5) default 0                comment '运费价格',
  lead_time         varchar(50)     default ''                 comment '供货周期',
  report_project        varchar(50)          default '非项目'	comment '报备项目 默认非项目',

  normal_flag char(1)         default '0'                comment '是否正常 0=正常 1=非正常 2销售经理审批结束 3 区域经理审批结束 4副总审批结束 5 老总审批结束',
  flow_status		varchar(2)         default '0' 	comment '流程状态0=未上报 1=销售员上报  2=销售经理同意 -2=销售经理不同意 3=区域经理同意 -3=区域经理不同意 4=副总同意 -4=副总不同意 5=老总同意 -5=老总不同意',

  remark         varchar(30)          default ''                  comment '备注',
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
  primary key (quotation_id)
) engine=innodb auto_increment=200 comment = '报价单表';


drop table if exists biz_quotation_product;
create table biz_quotation_product (
  qp_id           bigint(20)      not null auto_increment    comment 'ID',
  quotation_id         bigint(20)     default 0                 comment '报价表ID',
  product_id         bigint(20)      default 0                 comment '产品ID',
  product_ref1_id         bigint(20)      default 0                  comment '产品配件法兰id',
  product_ref1_num         varchar(30)      default 0                  comment '产品配件法兰数量',
  product_ref2_id         bigint(20)      default 0                  comment '产品配件螺栓id',
  product_ref2_num         varchar(30)      default 0                  comment '产品配件螺栓数量',
  actuator_id	bigint(20)      default 0                  comment '执行器id',
  remark         varchar(30)          default ''                  comment '备注',
  string1         varchar(30)          default ''                  comment '备用1',
  string2         varchar(30)          default ''                  comment '备用2',
  string3         varchar(30)          default ''                  comment '备用3',
  string4         varchar(30)          default ''                  comment '备用4',
  string5         varchar(30)          default ''                  comment '备用5',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (qp_id)
) engine=innodb auto_increment=200 comment = '报价单产品表';
