package io.zipcoder.tc_spring_poll_application.domain;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "VOTE_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "OPTION_ID")
    private Option option;

    public Vote() {
    }

    public Vote(Option option) {
        this.option = option;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
