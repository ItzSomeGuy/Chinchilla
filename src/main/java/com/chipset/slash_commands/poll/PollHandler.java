package com.chipset.slash_commands.poll;

import net.dv8tion.jda.api.entities.Member;

import java.util.*;

public class PollHandler {
    private ArrayList<Poll> polls;

    public PollHandler() {
        this.polls = new ArrayList<>();
    }

    public Poll getPoll(String question) throws Exception {
        for (Poll poll :
                this.polls) {
            if (poll.getQuestion().equals(question)) {
                return poll;
            }
        }
        throw new Exception("no poll found");
    }

    public void createPoll(String question, ArrayList<String> choices) {
        polls.add(new Poll(question, choices));
    }

    public class Poll {
        private String question;
        private Map<String, Integer> choices;
        private Map<Member, String> votes;
        private int totalVotes;
        private int top;

        public Poll(String question, ArrayList<String> choices) {

            this.question = question;
            this.choices = new LinkedHashMap<>();
            for (String choice :
                    choices) {
                this.choices.put(choice, 0);
            }
            this.votes = new HashMap<>();
            this.totalVotes = 0;
            final String[] emoji = {"⬛", "🟩", "🟥"};
        }

        public String getQuestion() {
            return question;
        }

        public Set<String> getChoices() {
            return this.choices.keySet();
        }

        public void setChoices(Map<String, Integer> choices) {
            this.choices = choices;
        }
        
        public int getChoiceVoteCount(String choice) {
            if (choices.containsKey(choice)) {
                return choices.get(choice);
            }
            return -1;
        }

        public int getTotalVotes() {
            return this.totalVotes;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getTop() {
            return this.top;
        }

        public void addChoice(String choice) {
            this.choices.put(choice, 0);
        }

        public void removeChoice(String choice) {
            this.choices.remove(choice);
        }

        public boolean hasMemberVoted(Member member) {
            for (Member m :
                    this.votes.keySet()) {
                if (m.equals(member)) {
                    return true;
                }
            }
            return false;
        }

        public void removeVote(String choice, Member member) {
            this.votes.remove(member); // remove member from votes
            this.choices.put(choice, this.choices.get(choice) - 1);
            this.totalVotes -= 1;

            int top = 1;
            for (String c :
                    this.getChoices()) {
                if (this.getChoiceVoteCount(c) > top) {
                    top = this.getChoiceVoteCount(c);
                }
            }

            this.top = top;
        }

        public boolean vote(String choice, Member member) {
            // check if user has voted
            if (hasMemberVoted(member)) { // tally re-vote
                for (Member m :
                        this.votes.keySet()) {
                    if (m.equals(member)) {
                        this.removeVote(this.votes.get(m), member);
                    }
                }

            }

            // tally vote
            votes.put(member, choice);
            // handle vote
            this.choices.put(choice, this.choices.get(choice) + 1);
            this.totalVotes += 1;

            //update top
            int top = 1;
            for (String c :
                    this.getChoices()) {
                if (this.getChoiceVoteCount(c) > top) {
                    top = this.getChoiceVoteCount(c);
                }
            }

            this.top = top;

            return true;
        }
    }
}
