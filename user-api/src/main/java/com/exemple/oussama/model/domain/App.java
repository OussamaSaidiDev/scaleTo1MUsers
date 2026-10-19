package com.exemple.oussama.model.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "applications")
public class App {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String author;
  private String name;
  private String version;

  @Builder
  public App(Long id, String author, String name, String version) {
    this.id = id;
    this.author = author;
    this.name = name;
    this.version = version;
  }

}
