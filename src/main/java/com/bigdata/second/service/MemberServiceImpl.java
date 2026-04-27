package com.bigdata.second.service;

import com.bigdata.second.entity.Member;
import com.bigdata.second.exception.ResourceNotFoundException;
import com.bigdata.second.exception.ValidationException;
import com.bigdata.second.repository.BorrowerRepository;
import com.bigdata.second.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BorrowerRepository borrowerRepository;

    public MemberServiceImpl(MemberRepository memberRepository,
                             BorrowerRepository borrowerRepository) {
        this.memberRepository = memberRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
    }

    @Override
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member update(Long id, Member newMember) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", id));
        member.setName(newMember.getName());
        member.setAddress(newMember.getAddress());
        member.setTelephone(newMember.getTelephone());
        return memberRepository.save(member);
    }

    @Override
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member", id);
        }
        // Aktif ödünç varsa silme
        if (borrowerRepository.existsByMemberIdAndReturnDateIsNull(id)) {
            throw new ValidationException(
                    "Bu üye silinemez çünkü üzerine kayıtlı aktif ödünç işlemi var. Önce kitabı iade alın."
            );
        }
        // Aktif ödünç yok → MySQL ON DELETE SET NULL devreye girer,
        // geçmiş borrower kayıtlarındaki member_id otomatik NULL'a çekilir.
        memberRepository.deleteById(id);
    }
}