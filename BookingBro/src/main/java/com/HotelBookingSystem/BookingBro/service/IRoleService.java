package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.Role;
import java.util.List;
import java.util.Optional;

public interface IRoleService {
    Role createRole(String name);
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Optional<Role> updateRole(Long id, String newName);
    boolean deleteRole(Long id);
}
