INSERT INTO `fmis`.`sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, '供方', '供方', 'buyer', NULL, NULL, 'Y', '0', 'admin', NULL, '', NULL, NULL);
INSERT INTO `fmis`.`sys_dict_data`(`dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ( 2, '需方', '需方', 'buyer', NULL, NULL, 'N', '0', 'admin', NULL, '', NULL, NULL);

alter table biz_process_data
add COLUMN except_pay_time varchar(100) default null comment '预计回款日期',
add COLUMN purchase_specific_requests varchar(255) default null comment '采购合同特殊要求';


