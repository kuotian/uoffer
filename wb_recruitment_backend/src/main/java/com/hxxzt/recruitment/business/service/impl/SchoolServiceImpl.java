/**
 * 项目名：recruitment
 * 日  期：2020/3/3
 * 包  名：com.hxxzt.recruitment.business.service.impl
 *
 * @author： niko_hxx
 */
package com.hxxzt.recruitment.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxxzt.recruitment.business.entity.views.School;
import com.hxxzt.recruitment.business.mapper.SchoolMapper;
import com.hxxzt.recruitment.business.service.ISchoolService;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {
}