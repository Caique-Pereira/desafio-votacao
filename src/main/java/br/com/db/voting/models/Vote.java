package br.com.db.voting.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
    @ManyToOne
    private Topic topic;
    private boolean vote;
    private Long associatedId;

}
