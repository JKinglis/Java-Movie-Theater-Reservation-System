package FinalProject;

import java.io.Serializable;

public class Movie implements Serializable {

	protected String title;
	protected int ranking;
	protected String review;

	Movie() {
		this.title = null;
		this.ranking = 0;
		this.review = null;
	}
	
	Movie(String title, int ranking, String review){
		this.title = title;
		this.ranking = ranking;
		this.review = review;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
	@Override
	public String toString() {
		return "Movie [title=" + title + ", ranking=" + ranking + ", review=" + review + "]";
	}

}
