package ASESpaghettiCode.UserServer.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Note {
    @Id
    private String noteId;

    private String noteTitle;

    private String authorId;

    private String coverImage;

    private String date;

    private Integer duration;

    private Double rating;

    private Integer expense;

    private Integer numTravelers;

    private String targetGroup;

    private String destination;

    private Double[] coordinates;

    private Object editorData;

    private List<String> likedUsers;


    public Note(String noteTitle, String authorId, String coverImage, String date, Integer duration, Double rating, Integer expense, Integer numTravelers, String targetGroup, String destination, Double[] coordinates, Object editorData) {
        this.noteTitle = noteTitle;
        this.authorId = authorId;
        this.coverImage = coverImage;
        this.date = date;
        this.duration = duration;
        this.rating = rating;
        this.expense = expense;
        this.numTravelers = numTravelers;
        this.targetGroup = targetGroup;
        this.destination = destination;
        this.coordinates = coordinates;
        this.editorData = editorData;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getExpense() {
        return expense;
    }

    public void setExpense(Integer expense) {
        this.expense = expense;
    }

    public Integer getNumTravelers() {
        return numTravelers;
    }

    public void setNumTravelers(Integer numTravelers) {
        this.numTravelers = numTravelers;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public Object getEditorData() {
        return editorData;
    }

    public void setEditorData(Object editorData) {
        this.editorData = editorData;
    }

    public List<String> getLikedUsers() {
        return this.likedUsers;
    }

    public void setLikedUsers(List<String> initialList) {
        this.likedUsers = initialList;
    }
    public void addLikedUsers(String userId) {
        this.likedUsers.add(userId);
    }

    public void removeLikedUsers(String userId) {
        this.likedUsers.remove(userId);
    }
}
