package Managefolio.admin.services;

import Managefolio.admin.model.Skill;
import java.util.List;

public interface SkillService {
    List<Skill> findByProfile(Long profileId);
    Skill findById(Long id);
    void save(Skill skill);
    void delete(Long id);
}