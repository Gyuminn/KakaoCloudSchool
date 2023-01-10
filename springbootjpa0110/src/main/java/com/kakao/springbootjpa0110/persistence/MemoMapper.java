package com.kakao.springbootjpa0110.persistence;

import com.kakao.springbootjpa0110.dto.MemoDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoMapper {
    @Select("select * from tbl_memo")
    public List<MemoDTO> listMemo();
}
