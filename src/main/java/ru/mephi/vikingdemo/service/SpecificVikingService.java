package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.Viking;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SpecificVikingService {
    private final VikingService vikingService;

    public SpecificVikingService(VikingService vikingService) {
        this.vikingService = vikingService;
    }



    public int countVikingsByAxes() {
        return (int) vikingService.findAll().stream().filter(viking -> {
                    long axeCount = viking.equipment().stream()
                            .filter(item -> item.name().toLowerCase().contains("axe")).count();
                    return axeCount == 1 || axeCount == 2;
                }).count();
    }





    public int countVikingsByBeardAndHair(String beard, String hair) {
        return (int)vikingService.findAll().stream()
                .filter(viking ->
                        viking.hairColor().name().equalsIgnoreCase(hair) &&
                                viking.beardStyle().name().equalsIgnoreCase(beard)
                ).count();
    }

    public int countVikingsByAgeInSpan(int start, int end){
        return (int)vikingService.findAll().stream()
                .filter(viking ->
                        viking.age() >= start && viking.age() <= end).count();
    }

    public int countVikingsByAgeWithoutSpan(int start, int end){
        return (int)vikingService.findAll().stream()
                .filter(viking ->
                        viking.age() < start || viking.age() > end).count();

    }

    public String getRandomVikingWithHeightMore180() {
        List<Viking> tallVikings = vikingService.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .collect(Collectors.toList());

        if (tallVikings.isEmpty()) {
            return "";
        }

        int randomIndex = new Random().nextInt(tallVikings.size());
        Viking viking = tallVikings.get(randomIndex);

        return (viking.name() + "\n Height: "+viking.heightCm()+ "\n BeardStyle: "+viking.beardStyle()+
                "\n HairColor: "+viking.hairColor()+"\n Age: " + viking.age() +"\n Equipment: " + formatEquipment(viking.equipment()));
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }


    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingService.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(e -> "Legendary".equalsIgnoreCase(e.quality())))
                .collect(Collectors.toList());
    }

    public List<Viking> getRedBeardedSortedByAge() {
        return vikingService.findAll().stream()
                .filter(v -> v.hairColor().name().equalsIgnoreCase("Red") ||
                        v.beardStyle().name().equalsIgnoreCase("Red"))
                .sorted((v1, v2) -> Integer.compare(v1.age(), v2.age()))
                .collect(Collectors.toList());
    }

    public List<Integer> getAllIds() {
        return vikingService.findAllIds();
    }


    public Integer getMaxId() {
        return getAllIds().stream()
                .max(Integer::compareTo)
                .orElse(0);
    }


    public List<Integer> getEvenIds() {
        return getAllIds().stream()
                .filter(id -> id % 2 == 0)
                .collect(Collectors.toList());
    }


    public String getMaxIdFormatted() {
        Integer maxId = getMaxId();
        if (maxId == 0) {
            return "In database there are not vikings";
        }
        return "Max ID: " + maxId + "\nTotal vikings: " + getAllIds().size();
    }


    public String getEvenIdsFormatted() {
        List<Integer> evenIds = getEvenIds();

        if (evenIds.isEmpty()) {
            return "Numbers which ID %2 == 0 - not exist";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Numbers which ID %2 == 0\n\n");
        sb.append("Count: ").append(evenIds.size()).append("\n");
        sb.append("IDs: ").append(evenIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        return sb.toString();
    }




}