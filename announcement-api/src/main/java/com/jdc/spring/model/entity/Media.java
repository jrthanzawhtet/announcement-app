package com.jdc.spring.model.entity;

import com.jdc.spring.model.constants.MediaType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "media")
public class Media {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type;
    
    @Column
    private String tags;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = true)
    private String link;

}
