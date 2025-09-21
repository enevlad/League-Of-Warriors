package accounts;

import entities.Character;

import java.util.*;

public class Account {
    private Information info;
    private ArrayList<entities.Character> characters;
    private int games_played;

    public Account(ArrayList<Character> characters, int games_played, Information info) {
        this.info = info;
        this.games_played = games_played;
        this.characters = characters;
    }

    public Information getInfo() {
        return info;
    }

    public int getGamesPlayed() {
        return games_played;
    }

    public void setGamesPlayed(int games_played) {
        this.games_played = games_played;
    }

    public ArrayList<entities.Character> getCharacters() {
        return characters;
    }

    @Override
    public String toString() {
        return info.toString() + "\n" +
                characters.toString() +
                "\nGames played: " + games_played;
    }

    public static class Information {
        private Credentials credentials;
        SortedSet<String> favoriteGames;
        private String name;
        private String country;

        public Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public Credentials getCredentials() {
            return credentials;
        }

        @Override
        public String toString() {
            return credentials.toString() +
                    favoriteGames.toString() +
                    "\nName: " + name +
                    "\nCountry: " + country;
        }

        public static class InformationBuilder {
            private Credentials credentials;
            SortedSet<String> favoriteGames;
            private String name;
            private String country;

            public InformationBuilder(Credentials credentials) {
                this.credentials = credentials;
            }

            public InformationBuilder favoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Account> accounts = JsonInput.deserializeAccounts();
        for (Account account : accounts) {
            System.out.println(account);
        }
    }
}
