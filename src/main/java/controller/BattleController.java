package controller;

import javafx.scene.paint.Color;
import model.editor.TileTypeEnum;
import model.editor.items.WeaponInfo;
import model.entity.BloodTypeEnum;
import model.entity.DirectionEnum;
import model.entity.GameModeEnum;
import model.entity.battle.DamageTypeEnum;
import model.entity.creatures.CreatureTypeEnum;
import model.entity.effects.EffectParams;
import model.entity.map.Creature;
import model.entity.map.Items;
import model.entity.map.MapCellInfo;
import model.entity.player.Character;
import view.Editor;
import view.Game;
import view.menu.MainMenu;

import java.util.List;

import static game.GameParams.mapSize;

/**
 * Боевые действия
 */
public class BattleController {

    public static final int baseFireDamage = 15; // базовый урон от горения
    public static final int baseAcidRainDamage = 3; // базовый урон от кислотного дождя
    public static final int baseAcidPollutionDamage = 3; // базовый урон от кислотного загрязнения

    /**
     * Персонаж атакует существо
     *
     * @param character - персонаж
     * @param weapon    - оружие, которым он атакует
     * @param creature  - атакуемое существо
     */
    public static void attackCreature(Character character, Items weapon, Creature creature) {
        WeaponInfo weaponInfo = ((WeaponInfo) weapon.getInfo());
        boolean isKilled = applyDamageToCreature(getDamageWithEffects(weapon, creature), DamageTypeEnum.valueOf(weaponInfo.getDamageType()), creature);
        if (isKilled) {
            if (Game.getMap().getSelecterCharacter() == character) { // выводим сообщение, только если атаковал текущий выбранный персонаж
                Game.showMessage(creature.getInfo().getName() + " " + Game.getText("KILLED"), Color.GREEN);
            }
        }
        ItemsController.damageItem(weapon, 1, Game.getMap().getSelecterCharacter().getInventory(), Game.getMap().getSelecterCharacter());
        int level = creature.getInfo().getLevel() != null ? creature.getInfo().getLevel() : 1;
        CharactersController.addSkillExp(((WeaponInfo) weapon.getInfo()).getSkill(), 5 * level);
    }

    /**
     * Персонаж атакует существо без оружия
     *
     * @param character - персонаж
     * @param damage    - наносимый урон
     * @param creature  - атакуемое существо
     */
    public static void attackCreature(Character character, int damage, Creature creature) {
        boolean isKilled = applyDamageToCreature(damage, DamageTypeEnum.CRUSHING_DAMAGE, creature);
        if (isKilled) {
            if (Game.getMap().getSelecterCharacter() == character) { // выводим сообщение, только если атаковал текущий выбранный персонаж
                Game.showMessage(creature.getInfo().getName() + " " + Game.getText("KILLED"), Color.GREEN);
            }
        }
        int level = creature.getInfo().getLevel() != null ? creature.getInfo().getLevel() : 1;
        CharactersController.addSkillExp("HAND_COMBAT", 5 * level);
    }

    /**
     * Персонаж игрока атакует другого персонажа
     *
     * @param character         - персонаж игрока
     * @param weapon            - оружие, которым он атакует
     * @param attackedCharacter - атакуемый attackedCharacter
     */
    public static void attackCharacter(Character character, Items weapon, Character attackedCharacter) {
        WeaponInfo weaponInfo = ((WeaponInfo) weapon.getInfo());
        boolean isKilled = applyDamageToCharacter(((WeaponInfo) weapon.getInfo()).getDamage(), DamageTypeEnum.valueOf(weaponInfo.getDamageType()), attackedCharacter); // TODO учесть броню attackedCharacter
        if (isKilled) {
            if (Game.getMap().getSelecterCharacter() == character) { // выводим сообщение, только если атаковал текущий выбранный персонаж
                Game.showMessage(attackedCharacter.getName() + " " + Game.getText("KILLED"), Color.GREEN);
            }
        }
        ItemsController.damageItem(weapon, 1, Game.getMap().getSelecterCharacter().getInventory(), Game.getMap().getSelecterCharacter());
        CharactersController.addSkillExp(((WeaponInfo) weapon.getInfo()).getSkill(), 10); // TODO скейл опыта от прокачки attackedCharacter
    }

    /**
     * Персонаж игрока атакует другого персонажа без оружия
     *
     * @param character         - персонаж игрока
     * @param damage            - наносимый урон
     * @param attackedCharacter - атакуемый attackedCharacter
     */
    public static void attackCharacter(Character character, int damage, Character attackedCharacter) {
        boolean isKilled = applyDamageToCharacter(damage, DamageTypeEnum.CRUSHING_DAMAGE, attackedCharacter); // TODO учесть броню attackedCharacter
        if (isKilled) {
            if (Game.getMap().getSelecterCharacter() == character) { // выводим сообщение, только если атаковал текущий выбранный персонаж
                Game.showMessage(attackedCharacter.getName() + " " + Game.getText("KILLED"), Color.GREEN);
            }
        }
        CharactersController.addSkillExp("HAND_COMBAT", 10); // TODO скейл опыта от прокачки attackedCharacter
    }

    /**
     * Наносит урон существу
     *
     * @param damagePoints - урон
     * @param damageType   - тип наносимого урона
     * @param creature     - существо
     * @return true- существо убито
     */
    public static boolean applyDamageToCreature(int damagePoints, DamageTypeEnum damageType, Creature creature) {
        creature.setHealth(creature.getHealth() - damagePoints);
        setBloodSplatter(damagePoints, damageType, creature);
        if (creature.getHealth() <= 0) {
            creature.setAlive(false);
            return true;
        }
        return false;
    }

    /**
     * Получить урон с модификаторами, наложенными на оружие
     *
     * @return значение урона с модификаторами
     */
    private static int getDamageWithEffects(Items weapon, Creature creature) {
        int damagePoints = ((WeaponInfo) weapon.getInfo()).getDamage();
        List<CreatureTypeEnum> creatureTypeEnums = creature.getInfo().getTypes();
        if (creatureTypeEnums != null && weapon.getEffects() != null) {
            for (CreatureTypeEnum type : creatureTypeEnums) {
                for (EffectParams effectParams : weapon.getEffects()) {
                    if (effectParams.getStrId().replaceAll("_DAMAGE_ADD", "").equals(type.name())) {
                        damagePoints += effectParams.getPower();
                    }
                }
            }
        }
        return damagePoints;
    }

    /**
     * Установить брызки крови при ударе по существу
     */
    private static void setBloodSplatter(int damagePoints, DamageTypeEnum damageType, Creature creature) {
        switch (damageType) {
            case CUTTING_DAMAGE: {
                if (damagePoints >= 10) {
                    setPollution(creature);
                }
                break;
            }
            case PIERCING_DAMAGE: {
                if (damagePoints >= 15) {
                    setPollution(creature);
                }
                break;
            }
            case CRUSHING_DAMAGE: {
                if (damagePoints >= 20) {
                    setPollution(creature);
                }
                break;
            }
        }
    }

    /**
     * Установить брызги на точку карты
     *
     * @param creature - существо
     */
    private static void setPollution(Creature creature) {
        String bloodTypeStr = creature.getInfo().getBloodType();
        BloodTypeEnum bloodType = bloodTypeStr == null ? BloodTypeEnum.BLOOD : BloodTypeEnum.valueOf(bloodTypeStr); // если тип крови не указан, берем обычную кровь
        MapCellInfo mapCellInfo = Game.getMap().getTiles()[creature.getXPos()][creature.getYPos()];
        if (mapCellInfo.getPollutionId() == 0) {
            mapCellInfo.setPollutionId(bloodType.getPollution1Id());
        } else if (mapCellInfo.getPollutionId() == bloodType.getPollution1Id()) {
            mapCellInfo.setPollutionId(bloodType.getPollution2Id());
        } else if (mapCellInfo.getPollutionId() == bloodType.getPollution2Id()) {
            mapCellInfo.setPollutionId(bloodType.getPollution3Id());
        }
    }

    /**
     * Наносит урон персонажу игрока
     *
     * @param damagePoints - урон
     */
    public static boolean applyDamageToCharacter(int damagePoints, DamageTypeEnum damageType, Character character) {
        damagePoints = getDamagePoint(damagePoints, damageType, character);
        var characterHealth = character.getParams().getIndicators().get(0).getCurrentValue();
        characterHealth = characterHealth - damagePoints;
        // при нанесение урона на земле остается кровь
        if (damageType.isBloody()) {
            Game.getMap().getTiles()[character.getXPosition()][character.getYPosition()].setPollutionId(1);
            MapController.drawTile(Game.getMap().getSelecterCharacter(), character.getXPos(), character.getYPos());
        }
        if (characterHealth <= 0) {
            character.setAlive(false);
            if (character.isActiveCharacter()) {
                killActiveCharacter();
            }
            return true;
        } else {
            character.getParams().getIndicators().get(0).setCurrentValue(characterHealth);
            return false;
        }
    }

    /**
     * Получить урон с учетом сопротивлений персонажа
     *
     * @param damagePoints - входящий урон без учета сопротивлений
     * @param damageType   - тип урона
     * @param character    - персонаж
     * @return - урон с учетом сопротивлений персонажа
     */
    private static int getDamagePoint(int damagePoints, DamageTypeEnum damageType, Character character) {
        switch (damageType) {
            case FIRE_DAMAGE: {
                for (EffectParams effect : character.getAppliedEffects()) {
                    if (effect.getStrId().equals("FIRE_DAMAGE_RESIST")) {
                        damagePoints -= effect.getPower();
                    }
                }
            }
            case ELECTRIC_DAMAGE: {
                for (EffectParams effect : character.getAppliedEffects()) {
                    if (effect.getStrId().equals("ELECTRIC_DAMAGE_RESIST")) {
                        damagePoints -= effect.getPower();
                    }
                }
            }
            case FROST_DAMAGE: {
                for (EffectParams effect : character.getAppliedEffects()) {
                    if (effect.getStrId().equals("FROST_DAMAGE_RESIST")) {
                        damagePoints -= effect.getPower();
                    }
                }
            }
            case ACID_DAMAGE: {
                for (EffectParams effect : character.getAppliedEffects()) {
                    if (effect.getStrId().equals("ACID_DAMAGE_RESIST")) {
                        damagePoints -= effect.getPower();
                    }
                }
            }
        }
        if (damagePoints < 0) {
            damagePoints = 0;
        }

        return damagePoints;
    }

    /**
     * Убивает выбранного персонажа игрока
     */
    public static void killActiveCharacter() {
        MainMenu.getPane().setVisible(true);
        Game.setGameMode(GameModeEnum.GAME_MENU);
        Game.showMessage(Game.getText("PLAYER_DIED"));
    }

    /**
     * Наносит урон по карте оружием
     *
     * @param mapCellInfo - точка карты
     * @param weapon      - оружие, которым наносится урон
     */
    public static void applyDamageToMapCell(MapCellInfo mapCellInfo, Items weapon) {
        applyDamageToMapCell(mapCellInfo, ((WeaponInfo) weapon.getInfo()).getDamage(),
                DamageTypeEnum.valueOf(((WeaponInfo) weapon.getInfo()).getDamageType()));
        if (weapon.getEffects() != null && weapon.getEffects().stream().anyMatch(i -> i.getStrId().equals("MOLD_STRIKE")) &&
                mapCellInfo.getTile1Type().equals(TileTypeEnum.EARTH)) {
            mapCellInfo.setTile1Id(MapController.getMoldGround()); // если в руке оружие с эффектом "плесневелый удар", то заражаем землю плесенью
        }
        String tileType = mapCellInfo.getTile2Info().getType();
        if (tileType != null && TileTypeEnum.valueOf(tileType).equals(TileTypeEnum.TRAINING_DUMMY)) { // если ударить тренировочный манекен, добавится опыт
            var skill = Game.getMap().getSelecterCharacter().getParams().getSkills().get(((WeaponInfo) weapon.getInfo()).getSkill());
            if (skill.getRealValue() < Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("maxTrainingLevel"))) { // опыт добавится, только если реальный навык ниже максимального уровня, до которого позволяет качаться манекен
                CharactersController.addSkillExp(((WeaponInfo) weapon.getInfo()).getSkill(), 5);
            } else {
                Game.showMessage(String.format(Game.getText("TRAIN_IMPOSSIBLE"), Game.getText(((WeaponInfo) weapon.getInfo()).getSkill() + "_PARAM_NAME")));
            }
        }
    }

    /**
     * Наносит урон точке на карте
     *
     * @param mapCellInfo  - точка
     * @param damagePoints - урон
     */
    public static void applyDamageToMapCell(MapCellInfo mapCellInfo, int damagePoints, DamageTypeEnum damageType) {
        if (mapCellInfo.getTile2Info().getStrength() > 0) {
            mapCellInfo.setTile2Strength(mapCellInfo.getTile2Strength() - damagePoints);
            if (mapCellInfo.getTile2Strength() <= 0) {
                mapCellInfo.setTile2Strength(0);
                if (damageType.equals(DamageTypeEnum.FIRE_DAMAGE)) {
                    mapCellInfo.setTile2Id(0);
                } else {
                    String tile2Type = mapCellInfo.getTile2Info().getType();
                    if (mapCellInfo.getTile2Info().getParams() != null && mapCellInfo.getTile2Info().getParams().get("wreckageId") != null) { // если явно указаны обломки, то берем их
                        mapCellInfo.setTile2Id(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("wreckageId")));
                    } else if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.WOOD)) {
                        mapCellInfo.setTile2Id(3); // пень
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(3).getStrength());
                    } else if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.ORE)) {
                        mapCellInfo.setTile2Id(241); // скала
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(241).getStrength());
                    } else if (tile2Type != null && TileTypeEnum.valueOf(tile2Type).equals(TileTypeEnum.CROPS)) {
                        mapCellInfo.setTile2Id(Integer.parseInt(mapCellInfo.getTile2Info().getParams().get("harvestedId"))); // для урожая берем то, что указано в параметрах
                    } else if (mapCellInfo.getTile2Id() == 241) {
                        MapController.addItemOnMap(mapCellInfo.getX(), mapCellInfo.getY(), new Items(ItemsController.stoneId, 10));
                        mapCellInfo.setTile2Id(0);
                    } else {
                        mapCellInfo.setTile2Id(0); // пустой тайл
                        mapCellInfo.setTile2Strength(Editor.getTiles2().get(0).getStrength());
                    }
                }
            }
        } else if (mapCellInfo.getTile1Info().getStrength() > 0) {
            mapCellInfo.setTile1Strength(mapCellInfo.getTile1Strength() - damagePoints);
            if (mapCellInfo.getTile1Strength() <= 0) {
                mapCellInfo.setTile1Strength(0);
                if (damageType.equals(DamageTypeEnum.FIRE_DAMAGE)) {
                    mapCellInfo.setTile1Id(MapController.getBurntGround());
                } else {
                    mapCellInfo.setTile1Id(37); // руины
                    mapCellInfo.setTile1Strength(Editor.getTiles1().get(37).getStrength());
                }
            }
        } else if (mapCellInfo.getTile2Info().getStrength() == 0 && mapCellInfo.getTile2Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
            mapCellInfo.setTile2Id(0);
        } else if (mapCellInfo.getTile1Info().getStrength() == 0 && mapCellInfo.getTile1Info().isBurn() &&
                damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && damagePoints == baseFireDamage * 4) {
            mapCellInfo.setTile1Id(MapController.getBurntGround());
        }

        if (damageType.equals(DamageTypeEnum.FIRE_DAMAGE) && mapCellInfo.getFireId() > 1) { // все загрязнения сгорают в огне
            mapCellInfo.setPollutionId(0);
        }

        // Предметы, лежащие не в контейнере могут сгореть или быть уничтожены взрывом
        if (((mapCellInfo.getTile2Info().getType() == null || !TileTypeEnum.valueOf(mapCellInfo.getTile2Info().getType()).equals(TileTypeEnum.CONTAINER)) &&
                (damageType.equals(DamageTypeEnum.FIRE_DAMAGE) || damageType.equals(DamageTypeEnum.EXPLOSIVE_DAMAGE))) &&
                mapCellInfo.getItems() != null) {
            List<Items> items = mapCellInfo.getItems();
            for (Items item : items) {
                if (item.getInfo().isBurn()) {
                    item.setCurrentStrength(item.getCurrentStrength() - damagePoints);
                    if (item.getCurrentStrength() <= 0) {
                        items.remove(item);
                    }
                }
            }
            if (items.size() > 0) {
                mapCellInfo.setItems(items);
            } else {
                mapCellInfo.setItems(null);
            }
        }
    }

    /**
     * Пинок персонажа или существа
     */
    public static void kick() {
        Character selectedCharacter = Game.getMap().getSelecterCharacter();
        DirectionEnum direction = selectedCharacter.getDirection();
        int targetX = selectedCharacter.getXPosition() + getKickShiftX(direction);
        int targetY = selectedCharacter.getYPosition() + getKickShiftY(direction);
        if (targetX >= 0 && targetY >= 0 && targetX < mapSize && targetY < mapSize) {
            MapCellInfo mapCellInfo = Game.getMap().getTiles()[targetX][targetY];
            if (mapCellInfo.getCreatureId() != null) {
                Creature creature = Game.getMap().getCreaturesList().get(mapCellInfo.getCreatureId());
                if (creature.isAlive()) {
                    BattleController.attackCreature(selectedCharacter, getKickDamagePoints(selectedCharacter), creature);
                }
                try {
                    MapCellInfo newMapCellInfo = Game.getMap().getTiles()[targetX + getKickShiftX(direction)][targetY + getKickShiftY(direction)];
                    if (MapController.isEmptyMapPoint(newMapCellInfo) &&
                            selectedCharacter.getInfo().getSize().getValue() >= creature.getInfo().getSize().getValue()) { // после пинка существа, равные либо меньше по размеру чем пинающий персонаж, отлетают в сторону
                        creature.setXPos(newMapCellInfo.getX());
                        creature.setYPos(newMapCellInfo.getY());
                        newMapCellInfo.setCreatureId(mapCellInfo.getCreatureId());
                        mapCellInfo.setCreatureId(null);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            } else if (mapCellInfo.getCharacterId() != null) {
                Character character = Game.getMap().getCharacterList().get(mapCellInfo.getCharacterId());
                if (character.isAlive()) {
                    BattleController.attackCharacter(selectedCharacter, getKickDamagePoints(selectedCharacter), character);
                }
                try {
                    MapCellInfo newMapCellInfo = Game.getMap().getTiles()[targetX + getKickShiftX(direction)][targetY + getKickShiftY(direction)];
                    if (MapController.isEmptyMapPoint(newMapCellInfo) &&
                            selectedCharacter.getInfo().getSize().getValue() >= character.getInfo().getSize().getValue()) { // после пинка персонажи, равные либо меньше по размеру чем пинающий персонаж, отлетают в сторону
                        character.setXPosition(newMapCellInfo.getX());
                        character.setYPosition(newMapCellInfo.getY());
                        newMapCellInfo.setCharacterId(mapCellInfo.getCharacterId());
                        mapCellInfo.setCharacterId(null);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        MapController.drawCurrentMap();
    }

    private static int getKickShiftX(DirectionEnum directionEnum) {
        switch (directionEnum) {
            case UP:
            case DOWN: {
                return 0;
            }
            case LEFT: {
                return -1;
            }
            case RIGHT: {
                return 1;
            }
        }
        return 0;
    }

    private static int getKickShiftY(DirectionEnum directionEnum) {
        switch (directionEnum) {
            case LEFT:
            case RIGHT: {
                return 0;
            }
            case UP: {
                return -1;
            }
            case DOWN: {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Получить размер урона, наносимого персонажем при помощи пинка
     *
     * @return размер урона
     */
    private static int getKickDamagePoints(Character character) {
        return 2;
    }
}
