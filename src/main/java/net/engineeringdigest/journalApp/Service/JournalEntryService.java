package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JouranalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {


    @Autowired
    private JouranalEntryRepository jouranalEntryRepository;
    @Autowired
    private UserService UserService;

    @Transactional
    public void saveEntry(JournalEntry journalEnty, String username) {
        User user = UserService.findByUsename(username);
        journalEnty.setDate(LocalDateTime.now());
        JournalEntry saved = jouranalEntryRepository.save(journalEnty);
        user.getJournalEntries().add(saved);
        // Don't call saveNewUSer here: it re-encodes password / resets roles.
        UserService.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEnty) {
        jouranalEntryRepository.save(journalEnty);
    }

    public List<JournalEntry> getAll() {
        return jouranalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return jouranalEntryRepository.findById(id);
    }
@Transactional
    public Boolean deleteById(ObjectId id, String username){
        boolean removed = false;
       try {
           User user = UserService.findByUsename(username);
            removed =  user.getJournalEntries().removeIf(x-> x.getId().equals(id));
           if(removed){
               UserService.saveUser(user);
               jouranalEntryRepository.deleteById(id);
           }
       }
       catch (Exception e){
           throw  new RuntimeException("Error deleting journal entry");
       }
       return removed;
    }

    public List<JournalEntry> findByUserName(String username) {
        User user = UserService.findByUsename(username);
        return user.getJournalEntries();

    }


}
