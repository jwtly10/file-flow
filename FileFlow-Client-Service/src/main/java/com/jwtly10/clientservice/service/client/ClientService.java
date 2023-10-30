package com.jwtly10.clientservice.service.client;

import com.jwtly10.common.models.UploadFile;
import com.jwtly10.databaseservice.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final SupabaseService supabaseService;

    public String getUserId(String username) {
        return supabaseService.getUser(username).getUser_Id();
    }

    public void logFile(UploadFile uploadFile) {
        supabaseService.createUploadedFile(uploadFile);
    }
}
