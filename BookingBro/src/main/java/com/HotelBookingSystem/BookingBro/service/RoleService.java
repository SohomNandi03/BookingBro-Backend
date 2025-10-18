package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.Role;
import com.HotelBookingSystem.BookingBro.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(String name) {
        if (roleRepository.findByName(name) != null) {
            throw new RuntimeException("Role already exists!");
        }
        return roleRepository.save(new Role(name));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> updateRole(Long id, String newName) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setName(newName);
                    return roleRepository.save(role);
                });
    }

    @Override
    public boolean deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
