package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Reservations;
import com.example.restaurants.data.models.Tables;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.ReservationsRepository;
import com.example.restaurants.repositories.TablesRepository;
import com.example.restaurants.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationsService {

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private UsersRepository usersRepository;

    private LocalDateTime to;
    private LocalDateTime from;
    private ArrayList<LocalDateTime> offers = null;
    private Tables offerTable = null;
    private ArrayList<Tables> offerTables = null;

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public ResponseEntity saveFunction(String date, String time, String username, String restaurant, Integer type){
        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        LocalDateTime localDateTimeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        from = localDateTimeFrom;
        to = localDateTimeFrom;

        boolean goodResTime = addCorrespondingTime(date, type);

        LocalDateTime localDateTimeTo = null;

        if(goodResTime) localDateTimeTo = to;
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Reservations reservation = new Reservations();

        Tables table = null;

        Users user = usersRepository.findByEmail(username);

        if(offerTables!=null && offerTables.size()!=0){
            Tables tables = new Tables();
            tables.setReserved(true);
            tables.setRestaurant(offerTables.get(0).getRestaurant());
            tables.setType(offerTables.get(0).getType()+offerTables.get(1).getType());
            tablesRepository.save(tables);
            List<Tables> tablesRes = tablesRepository.findTablesByTypeAndRestaurant_Id(tables.getType(), tables.getRestaurant().getId());
            reservation.setTable(tablesRes.get(0));
            reservation.setTimeFrom(localDateTimeFrom);
            reservation.setTimeTo(localDateTimeTo);
            reservation.setUser(user);

            reservationsRepository.save(reservation);
            for(int i=0; i<offerTables.size(); i++) {
                offerTables.get(i).setReserved(true);
                tablesRepository.save(offerTables.get(i));
            }

            return new ResponseEntity(HttpStatus.OK);
        }
        else if(offerTable == null) table = tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type, restaurant);
        else table = offerTable;

        reservation.setTable(table);
        reservation.setTimeFrom(localDateTimeFrom);
        reservation.setTimeTo(localDateTimeTo);
        reservation.setUser(user);

        reservationsRepository.save(reservation);
        table.setReserved(true);
        tablesRepository.save(table);

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity saveByTimeFunction(String date, String time, String timeFrom,String username, String restaurant, Integer type){
        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        LocalDateTime localDateTimeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        LocalDateTime localDateTimeTo = localDateTimeFrom.plusHours(1);

        if(timeFrom.charAt(0)!='0' && timeFrom.charAt(1)==':') timeFrom='0'+timeFrom;
        String datetimeWanted = date + " " +  timeFrom;
        LocalDateTime localDateTimeWanted = LocalDateTime.parse(datetimeWanted, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        Reservations reservation = new Reservations();
        Users user = usersRepository.findByEmail(username);

        if (offerTables != null && offerTables.size() != 0) {
            reservation.setTable(offerTables.get(0));
            reservation.setTimeFrom(localDateTimeFrom);
            reservation.setTimeTo(localDateTimeTo);
            reservation.setUser(user);

            reservationsRepository.save(reservation);
            for(int i=0; i<offerTables.size(); i++) {
                offerTables.get(i).setReserved(true);
                tablesRepository.save(offerTables.get(i));
            }

            return new ResponseEntity(HttpStatus.OK);
        }

        else if(offerTable != null){
            reservation.setTable(offerTable);
            reservation.setTimeFrom(localDateTimeFrom);
            reservation.setTimeTo(localDateTimeTo);
            reservation.setUser(user);

            reservationsRepository.save(reservation);
            offerTable.setReserved(true);
            tablesRepository.save(offerTable);

            return new ResponseEntity(HttpStatus.OK);
        }

        else {
            List<Reservations> existingReservation = reservationsRepository.findByTable_TypeAndTable_Restaurant_NameAndTimeFromIsLessThanEqual
                    (type, restaurant, localDateTimeWanted);

            reservation.setUser(user);
            reservation.setTable(existingReservation.get(0).getTable());
            reservation.setTimeFrom(localDateTimeFrom);
            reservation.setTimeTo(localDateTimeTo);

            reservationsRepository.save(reservation);

            return new ResponseEntity(HttpStatus.OK);
        }
    }

    public ResponseEntity checkTable(String date, String time, String username, String restaurant, Integer type){
        offerTable = null;
        offerTables = null;
        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        String startDateTime = date + " " + "08:00";
        String endDateTime = date + " " + "23:59";
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        LocalDateTime startingTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        LocalDateTime endingTime = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        from = localDateTime;
        to = localDateTime;
        addCorrespondingTime(date, type);

        if(localDateTime.isAfter(startingTime) && localDateTime.isBefore(endingTime)){
            Reservations existingReservation = reservationsRepository.findByUser_EmailAndTable_TypeAndTable_Restaurant_Name
                    (username, type, restaurant);

            if(existingReservation != null) {
                LocalDateTime timeFrom = existingReservation.getTimeFrom();
                LocalDateTime timeTo = existingReservation.getTimeTo();
                if((localDateTime.isAfter(timeFrom) && localDateTime.isBefore(timeTo)) || (to.isAfter(timeFrom) && to.isBefore(timeTo)) || localDateTime.equals(timeFrom) || to.equals(timeTo)){
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
                else return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            }

            Reservations takenReservation = reservationsRepository.findFirstByTable_TypeAndTable_Restaurant_Name
                    (type, restaurant);
            Tables freeTable = tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type, restaurant);

            if(freeTable == null && takenReservation != null) {
                LocalDateTime timeFrom = takenReservation.getTimeFrom();
                LocalDateTime timeTo = takenReservation.getTimeTo();
                if((localDateTime.isAfter(timeFrom) && localDateTime.isBefore(timeTo)) || (to.isAfter(timeFrom) && to.isBefore(timeTo)) || localDateTime.equals(timeFrom) || to.equals(timeTo)) {
                    offers = new ArrayList<LocalDateTime>();
                    searchOffers(true, true, type, localDateTime, restaurant);
                    return new ResponseEntity(offers, HttpStatus.CONFLICT);
                }
                else return new ResponseEntity(HttpStatus.BAD_GATEWAY);
            }

            else if(freeTable != null){
                from = localDateTime;
                to = localDateTime;
                boolean check = addCorrespondingTime(date, type);

                if(!check){
                    offers = new ArrayList<LocalDateTime>();
                    datetime = date + " " + "21:00";
                    LocalDateTime timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                    if(type > 7 && localDateTime.isAfter(timeTo)){
                        searchOffers(true, false, type, localDateTime, restaurant);
                    }
                    datetime = date + " " + "21:30";
                    timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                    if(type > 4 && type <= 7 && localDateTime.isAfter(timeTo)){
                        searchOffers(true, false, type, localDateTime, restaurant);
                    }
                    datetime = date + " " + "22:00";
                    timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                    if(type <= 4  && localDateTime.isAfter(timeTo)){
                        searchOffers(true, false, type, localDateTime, restaurant);
                    }
                    return new ResponseEntity(offers, HttpStatus.CONFLICT);
                }

                else return new ResponseEntity(HttpStatus.OK);
            }

            else{
                //todo: check if there is a closest similar table
                Tables table = findNearest(type);
                if(table!=null && (((table.getType() - type) > 0) && ((table.getType() - type) <= 2))){
                    offerTable = table;
                }
                //todo: combination of tables method
                else{
                    makeCombinations(type, restaurant);
                }

                if(offerTable!=null || (offerTables!=null && offerTables.size()!=0)){
                    from = localDateTime;
                    to = localDateTime;
                    boolean check = addCorrespondingTime(date, type);

                    if(!check){
                        offers = new ArrayList<LocalDateTime>();
                        datetime = date + " " + "21:00";
                        LocalDateTime timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                        if(type > 7 && localDateTime.isAfter(timeTo)){
                            searchOffers(true, false, type, localDateTime, restaurant);
                        }
                        datetime = date + " " + "21:30";
                        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                        if(type > 4 && type <= 7 && localDateTime.isAfter(timeTo)){
                            searchOffers(true, false, type, localDateTime, restaurant);
                        }
                        datetime = date + " " + "22:00";
                        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
                        if(type <= 4  && localDateTime.isAfter(timeTo)){
                            searchOffers(true, false, type, localDateTime, restaurant);
                        }
                        return new ResponseEntity(offers, HttpStatus.CONFLICT);
                    }

                    else return new ResponseEntity(HttpStatus.OK);
                }

                else return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    public boolean addCorrespondingTime(String date, Integer type){
        String datetime = date + " " + "08:00";
        LocalDateTime timeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        datetime = date + " " + "10:01";
        LocalDateTime timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo)){
            if(type >= 2 && type <= 4) to = to.plusHours(1);
            else if(type > 4 && type <= 7) {
                to = to.plusHours(1);
                to = to.plusMinutes(30);
            }
            else if(type > 7) to = to.plusHours(2);
            return true;
        }


        datetime = date + " " + "10:00";
        timeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        datetime = date + " " + "13:01";
        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo)){
            if(type == 2) to = to.plusHours(1);
            else if(type > 2 && type <= 7) {
                to = to.plusHours(1);
                to = to.plusMinutes(30);
            }
            else if(type > 7) to = to.plusHours(2);
            return true;
        }


        datetime = date + " " + "13:00";
        timeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        datetime = date + " " + "18:01";
        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo)){
            if(type >= 2 && type <= 4) {
                to = to.plusHours(1);
                to = to.plusMinutes(30);
            }
            else if(type > 4) to = to.plusHours(2);
            return true;
        }



        datetime = date + " " + "18:00";
        timeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        datetime = date + " " + "21:01";
        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo) && type > 7){
            to = to.plusHours(3);
            return true;
        }

        datetime = date + " " + "21:31";
        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo) && (type > 4 && type <= 7)){
            to = to.plusHours(2);
            to = to.plusMinutes(30);
            return true;
        }

        datetime = date + " " + "22:01";
        timeTo = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
        if(from.isAfter(timeFrom) && from.isBefore(timeTo) && (type >= 2 && type <= 4)){
            to = to.plusHours(2);
            return true;
        }

        return false;
    }



    private void searchOffers(boolean backward, boolean forward, Integer tableType, LocalDateTime date, String res_name){
        //todo: do search with time limit +/- 2h and populate list offers
        //TODO: ALSO CHECK IF IT GOES OUT OF WORKING TIME RANGE!!!
        LocalDateTime backwardTimeLimit = date.minusHours(2);
        LocalDateTime forwardTimeLimit = to.plusHours(2);
        LocalDateTime time = date;
        if(backward){
            while(time.isAfter(backwardTimeLimit)){
                LocalDateTime timeTemporary = time.minusMinutes(30);
                Reservations res = reservationsRepository.findByTable_TypeAndTable_ReservedAndTimeFromAndTable_Restaurant_Name(tableType, true, timeTemporary, res_name);
                long hours = ChronoUnit.HOURS.between(date, to);
                LocalDateTime temp = date.plusHours(hours);
                long minutes = ChronoUnit.MINUTES.between(temp, to);
                temp = timeTemporary.plusHours(hours);
                LocalDateTime temp2 = temp.plusMinutes(minutes);
                if(res == null && temp2.isBefore(from)) offers.add(timeTemporary);
                time = timeTemporary;
            }
        }
        time=date;
        if(forward){
            while(time.isBefore(forwardTimeLimit)){
                LocalDateTime timeTemporary = time.plusMinutes(30);
                Reservations res = reservationsRepository.findByTable_TypeAndTable_ReservedAndTimeFromAndTable_Restaurant_Name(tableType, true, timeTemporary, res_name);
                if(res == null && timeTemporary.isAfter(to)) offers.add(timeTemporary);
                time = timeTemporary;
            }
        }
    }

    public Tables findNearest(Integer type){
        Integer typeTwo = type + 3;
        Tables table = null;
        List<Tables> tables = tablesRepository.findTablesByTypeIsBetween(type, typeTwo);
        if(tables!=null && tables.size()!=0 )return tables.get(0);
        else return table;
    }

    public void makeCombinations(Integer type, String restaurant){
        ArrayList<Tables> tables = tablesRepository.findByReservedIsFalseAndRestaurant_Name(restaurant);
        ArrayList<Integer> tablesTypes = new ArrayList<Integer>();
        for(int i=0; i<tables.size(); i++){
            tablesTypes.add(tables.get(i).getType());
        }
        offerTables = new ArrayList<Tables>();
        for(int i=0; i<tablesTypes.size(); i++){
            if(tablesTypes.contains(type-tablesTypes.get(i))){
                Integer type1 = type - tablesTypes.get(i);
                Integer type2 = tablesTypes.get(i);
                offerTables.add(tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type1, restaurant));
                offerTables.add(tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type2, restaurant));
                break;
            }
            else if(tablesTypes.contains(type-tablesTypes.get(i)+1)){
                Integer type1 = type - tablesTypes.get(i)+1;
                Integer type2 = tablesTypes.get(i);
                offerTables.add(tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type1, restaurant));
                offerTables.add(tablesRepository.findFirstByTypeAndReservedIsFalseAndRestaurant_Name(type2, restaurant));
                break;
            }
        }
    }
}
