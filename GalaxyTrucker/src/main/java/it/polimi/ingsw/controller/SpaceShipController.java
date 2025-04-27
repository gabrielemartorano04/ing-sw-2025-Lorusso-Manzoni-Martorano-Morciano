package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidCoordinates;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotEnoughBatteries;
import it.polimi.ingsw.model.Bank;
import it.polimi.ingsw.model.SpaceShip;
import it.polimi.ingsw.model.components.Component;
import it.polimi.ingsw.model.components.Direction;
import it.polimi.ingsw.model.components.Goods;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SpaceShipController {
    private final SpaceShip spaceShip;

    public SpaceShipController(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public ArrayList<Component> getKept() {
        return spaceShip.getKept();
    }

    public void increaseLost(int lost) {
        spaceShip.increaseLost(lost);
    }

    public void setBattery() {
        spaceShip.setBattery();
    }

    public int getGoodsCapacity() {
        return spaceShip.getGoodsCapacity();
    }

    public void setNumHuman() {
        spaceShip.setNumHuman();
    }

    public int getNumHuman() {
        return spaceShip.getNumHuman();
    }

    public void setNumAlien(ArrayList<int[]> coord){
        spaceShip.setNumAlien(coord);
    }

    public void removeHuman(int x, int y){
        spaceShip.removeHuman(x, y);
    }

    public void removeNumHuman(ArrayList<int[]> coord){
        spaceShip.removeNumHuman(coord);
    }

    public Component[][] getComponents() {
        return spaceShip.getComponents();
    }

    public int getNumDoubleCannon(){
        return spaceShip.getNumDoubleCannon();
    }

    public int getNumDoubleEngine(){
        return spaceShip.getNumDoubleEngine();
    }

    public void addComponent(Component component, int x, int y) throws InvalidCoordinates {
        try {
            spaceShip.addComponent(component, x, y);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    public Component removeComponent(int x, int y) throws InvalidCoordinates {
        try {
            return spaceShip.removeComponent(x, y);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    public int getSingleCannonPower() {
        return spaceShip.getSingleCannonPower();
    }

    public int getDoubleCannon() {
        return spaceShip.getDoubleCannon();
    }

    public int getSingleEnginePower() {
        return spaceShip.getSingleEnginePower();
    }

    public int getDoubleEngine() {
        return spaceShip.getDoubleEngine();
    }

    public Component chooseComponent(int x, int y) {
        return spaceShip.chooseComponent(x, y);
    }

    public ArrayList<Direction> getProtection() {
        return spaceShip.getProtection();
    }

    public boolean isValid(int x, int y) {
        return spaceShip.isValid(x, y);
    }

    public int getTotalCannonPower(ArrayList<int[]> coord) {
        return spaceShip.getTotalCannonPower(coord);
    }

    public int getTotalEnginePower(ArrayList<int[]> coord) {
        return spaceShip.getTotalEnginePower(coord);
    }

    public int countExposedConnectors() {
        return spaceShip.countExposedConnectors();
    }

    public boolean wellDone() {
        return spaceShip.wellDone();
    }

    public boolean isHit(int direction, int num) {
        return spaceShip.isHit(direction, num);
    }

    public void destroyComponent(int num, int direction, Bank bank) {
        spaceShip.destroyComponent(num, direction, bank);
    }

    public boolean shootMeteor(int direction, int num) {
        return spaceShip.shootMeteor(direction, num);
    }

    public boolean blockSmallMeteor(int direction, int num, boolean use) {
        return spaceShip.blockSmallMeteor(direction, num, use);
    }

    public boolean blockLightCannonShot(int direction,int num, boolean use){
        return spaceShip.blockLightCannonShot(direction, num, use);
    }

    public void epidemic() {
        spaceShip.epidemic();
    }

    public void addGoodsList(Goods good){
        spaceShip.addGoodsList(good);
    }

    public void addGoods(List<Goods> goods, ArrayList<int[]> coord, Bank bank){
        spaceShip.addGoods(goods, coord, bank);
    }

    public void addGood(Goods good,int[] coord,int value ,Bank bank) throws InvalidPositionException {
        try {
            spaceShip.addGood(good, coord, value , bank);
        } catch (InvalidPositionException e) {
            throw new InvalidPositionException(e.getMessage());
        }
    }

    public ArrayList<Goods> getGoodsList(){
        return spaceShip.getGoodsList();
    }

    public int getLost(){
        return spaceShip.getLost();
    }

    public void removeBatteries(ArrayList<int[]> batteryCoordinates)throws NotEnoughBatteries {
        try {
            spaceShip.removeBatteries(batteryCoordinates);
        } catch (NotEnoughBatteries e) {
            throw new NotEnoughBatteries(e.getMessage());
        }
    }

    public void removeAllGoods(){
        spaceShip.removeAllGoods();
    }

    public int getNumBatteries(){
        return spaceShip.getNumBatteries();
    }

    public boolean checkLoad(ArrayList<int[]> coord){
        return spaceShip.checkLoad(coord);
    }

    public boolean checkLoad(int[] c){
        return spaceShip.checkLoad(c);
    }

    public boolean checkDoubleEngine(ArrayList<int[]> coord){
        return spaceShip.checkDoubleEngine(coord);
    }

    public boolean checkDoubleCannon(ArrayList<int[]> coord){
        return spaceShip.checkDoubleCannon(coord);
    }

    public boolean checkShield(ArrayList<int[]> coord){
        return spaceShip.checkShield(coord);
    }

    public boolean checkBatteryStorage(ArrayList<int[]> coord){
        return spaceShip.checkBatteryStorage(coord);
    }

    public boolean checkCabin(ArrayList<int[]> coord, int sum) throws InvalidCoordinates{
        try {
            return spaceShip.checkCabin(coord, sum);
        } catch (InvalidCoordinates e) {
            throw new InvalidCoordinates(e.getMessage());
        }
    }

    public void swapGoods(int[] coord1, int value1, int[] coord2, int value2) throws InvalidPositionException{
        try {
            spaceShip.swapGoods(coord1, value1, coord2, value2);
        } catch (InvalidPositionException e) {
            throw new InvalidPositionException(e.getMessage());
        }
    }

    public int getRedGoods(){
        return spaceShip.getRedGoods();
    }

    public int getGreenGoods(){
        return spaceShip.getGreenGoods();
    }

    public int getBlueGoods(){
        return spaceShip.getBlueGoods();
    }

    public int getYellowGoods() {
        return spaceShip.getYellowGoods();
    }

    public void removeGoods(ArrayList<Goods> list){
        spaceShip.removeGoods(list);
    }
}
