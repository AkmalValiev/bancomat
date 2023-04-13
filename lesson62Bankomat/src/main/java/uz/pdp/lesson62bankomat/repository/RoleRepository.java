package uz.pdp.lesson62bankomat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson62bankomat.entity.Role;
import uz.pdp.lesson62bankomat.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);

}
