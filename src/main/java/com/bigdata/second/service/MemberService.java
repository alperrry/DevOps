package com.bigdata.second.service;


import com.bigdata.second.entity.Member;

import java.util.List;

public interface MemberService {
    List<Member> getAllMembers();

    Member findById(Long id);

    Member createMember(Member member);

    // UPDATE
    Member update(Long id, Member newMember);

    // DELETE
    void delete(Long id);
}

