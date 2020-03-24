package com.mobstac.thehindubusinessline.model;

import java.util.List;

/**
 * Created by arvind on 16/8/16.
 */
public class Section {

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private int date;

        private HomeEntity home;

        private List<SectionEntity> section;
        private List<SectionEntity> Portfolio;
        private List<SectionEntity> BLink;

        public List<SectionEntity> getPortfolio() {
            return Portfolio;
        }

        public void setPortfolio(List<SectionEntity> portfolio) {
            Portfolio = portfolio;
        }

        public List<SectionEntity> getBLink() {
            return BLink;
        }

        public void setBLink(List<SectionEntity> BLink) {
            this.BLink = BLink;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public HomeEntity getHome() {
            return home;
        }

        public void setHome(HomeEntity home) {
            this.home = home;
        }

        public List<SectionEntity> getSection() {
            return section;
        }

        public void setSection(List<SectionEntity> section) {
            this.section = section;
        }

        public static class HomeEntity {

            public StaticPageUrl staticPageUrl;

            private BannerEntity banner;

            private List<ExploreEntity> explore;


            private List<WidgetEntity> widget;


            private List<PersonalizeEntity> personalize;

            public BannerEntity getBanner() {
                return banner;
            }

            public void setBanner(BannerEntity banner) {
                this.banner = banner;
            }

            public List<ExploreEntity> getExplore() {
                return explore;
            }

            public void setExplore(List<ExploreEntity> explore) {
                this.explore = explore;
            }

            public List<WidgetEntity> getWidget() {
                return widget;
            }

            public void setWidget(List<WidgetEntity> widget) {
                this.widget = widget;
            }

            public List<PersonalizeEntity> getPersonalize() {
                return personalize;
            }

            public void setPersonalize(List<PersonalizeEntity> personalize) {
                this.personalize = personalize;
            }

            /*public static class StaticPageUrl {
                private String url ;
                private boolean isEnabled;
                private String position;
                 private int sectionId;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public boolean isEnabled() {
                    return isEnabled;
                }

                public void setEnabled(boolean enabled) {
                    isEnabled = enabled;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public int getSectionId() {
                    return sectionId;
                }

                public void setSectionId(int sectionId) {
                    this.sectionId = sectionId;
                }
            }*/

            public static class BannerEntity {
                private int secId;
                private String secName;
                private String type;

                public int getSecId() {
                    return secId;
                }

                public void setSecId(int secId) {
                    this.secId = secId;
                }

                public String getSecName() {
                    return secName;
                }

                public void setSecName(String secName) {
                    this.secName = secName;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class ExploreEntity {
                private int secId;
                private int homePriority;
                private int overridePriority;
                private String secName;
                private String image;
                private String image_v2;
                private String type;

                public int getSecId() {
                    return secId;
                }

                public void setSecId(int secId) {
                    this.secId = secId;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                public int getHomePriority() {
                    return homePriority;
                }

                public void setHomePriority(int homePriority) {
                    this.homePriority = homePriority;
                }

                public int getOverridePriority() {
                    return overridePriority;
                }

                public void setOverridePriority(int overridePriority) {
                    this.overridePriority = overridePriority;
                }

                public String getSecName() {
                    return secName;
                }

                public void setSecName(String secName) {
                    this.secName = secName;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getImage_v2() {
                    return image_v2;
                }

                public void setImage_v2(String image_v2) {
                    this.image_v2 = image_v2;
                }
            }

            public static class WidgetEntity {
                private int secId;
                private int parentSecId;
                private int homePriority;
                private int overridePriority;
                private String secName;
                private String type;

                public int getSecId() {
                    return secId;
                }

                public void setSecId(int secId) {
                    this.secId = secId;
                }

                public int getHomePriority() {
                    return homePriority;
                }

                public void setHomePriority(int homePriority) {
                    this.homePriority = homePriority;
                }

                public int getOverridePriority() {
                    return overridePriority;
                }

                public void setOverridePriority(int overridePriority) {
                    this.overridePriority = overridePriority;
                }

                public int getParentSecId() {
                    return parentSecId;
                }

                public void setParentSecId(int parentSecId) {
                    this.parentSecId = parentSecId;
                }

                public String getSecName() {
                    return secName;
                }

                public void setSecName(String secName) {
                    this.secName = secName;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class PersonalizeEntity {
                private int secId;
                private int homePriority;
                private int overridePriority;
                private String secName;
                private String type;

                public int getSecId() {
                    return secId;
                }

                public void setSecId(int secId) {
                    this.secId = secId;
                }

                public int getHomePriority() {
                    return homePriority;
                }

                public void setHomePriority(int homePriority) {
                    this.homePriority = homePriority;
                }

                public int getOverridePriority() {
                    return overridePriority;
                }

                public void setOverridePriority(int overridePriority) {
                    this.overridePriority = overridePriority;
                }

                public String getSecName() {
                    return secName;
                }

                public void setSecName(String secName) {
                    this.secName = secName;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }

        public static class SectionEntity {
            private int parentId;
            private int secId;
            private String secName;
            private String secColorRgb;
            private String type;
            private int priority;
            private int overridePriority;
            private String image;
            private String image_v2;
            private boolean show_on_burger;
            private boolean show_on_explore;
            private List<SectionEntity> subSections;
            private String link;
            private int customScreen;
            private int customScreenPri;
            private String webLink;
            public StaticPageUrl staticPageUrl;

            public int getCustomScreenPri() {
                return customScreenPri;
            }

            public void setCustomScreenPri(int customScreenPri) {
                this.customScreenPri = customScreenPri;
            }

            public int getCustomScreen() {
                return customScreen;
            }

            public void setCustomScreen(int customScreen) {
                this.customScreen = customScreen;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getSecId() {
                return secId;
            }

            public void setSecId(int secId) {
                this.secId = secId;
            }

            public String getSecName() {
                return secName;
            }

            public void setSecName(String secName) {
                this.secName = secName;
            }

            public String getSecColorRgb() {
                return secColorRgb;
            }

            public void setSecColorRgb(String secColorRgb) {
                this.secColorRgb = secColorRgb;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public int getOverridePriority() {
                return overridePriority;
            }

            public void setOverridePriority(int overridePriority) {
                this.overridePriority = overridePriority;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public boolean isShow_on_burger() {
                return show_on_burger;
            }

            public void setShow_on_burger(boolean show_on_burger) {
                this.show_on_burger = show_on_burger;
            }

            public boolean isShow_on_explore() {
                return show_on_explore;
            }

            public void setShow_on_explore(boolean show_on_explore) {
                this.show_on_explore = show_on_explore;
            }

            public List<SectionEntity> getSubSections() {
                return subSections;
            }

            public void setSubSections(List<SectionEntity> subSections) {
                this.subSections = subSections;
            }

            public String getWebLink() {
                return webLink;
            }

            public void setWebLink(String webLink) {
                this.webLink = webLink;
            }

            public String getImage_v2() {
                return image_v2;
            }

            public void setImage_v2(String image_v2) {
                this.image_v2 = image_v2;
            }
        }
    }
}
