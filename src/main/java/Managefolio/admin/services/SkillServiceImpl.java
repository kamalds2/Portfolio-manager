package Managefolio.admin.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Managefolio.admin.model.Skill;
import Managefolio.admin.repository.SkillRepository;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired private SkillRepository skillRepository;

    @Override
    public List<Skill> findByProfile(Long profileId) {
        return skillRepository.findByProfileId(profileId);
    }

    @Override
    public Skill findById(Long id) {
        return skillRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    @Override
    public void save(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public void delete(Long id) {
        skillRepository.deleteById(id);
    }
}