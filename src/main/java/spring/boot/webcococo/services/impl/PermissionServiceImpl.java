package spring.boot.webcococo.services.impl;

import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Permission;
import spring.boot.webcococo.repositories.PermissionRepository;
import spring.boot.webcococo.services.IPermissionService;


@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Integer, PermissionRepository> implements IPermissionService {
    public PermissionServiceImpl(PermissionRepository repository) {
        super(repository);
    }
    //
//


}
