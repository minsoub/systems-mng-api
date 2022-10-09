package com.bithumbsystems.persistence.mongodb.rsacipherinfo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "rsa_cipher_info")
@NoArgsConstructor
@AllArgsConstructor
public class RsaCipherInfo {
    @Id
    private String id;

    private String serverPrivateKey;
    private String serverPublicKey;

    private LocalDateTime createdAt;
}
