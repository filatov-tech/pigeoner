class SectionHierarchy extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoaded: false,
            sections: []
        }
    }

    componentDidMount() {
        fetch("http://localhost:8080/api/v1/sections/hierarchy")
            .then(response => response.json())
            .then(
                result => {
                    this.setState({
                        isLoaded: true,
                        sections: result
                    })
                },
                error => {
                    this.setState({
                        isLoaded: true,
                        error
                    })
                }
            )
    }
    
    render() {
        const {error, isLoaded, sections} = this.state;
        let rootLevel = '';
        if (error) {
            return <div>Ошибка! {error.message}</div>;
        } else if (!isLoaded) {
            return <div>Loading...</div>
        } else {
            return (
                <select id="location" className="form-select" name="location">
                    <option value="">Выберите голубятню</option>
                    {sections.map(section => (
                        <SectionOption section={section} hierarchyLevel={rootLevel}/>
                    ))}
                </select>
            );
        }
    }
}

class SectionOption extends React.Component {
    static defaultProps = {
        prefixElement: '\xa0\xa0\u23B9\xa0\xa0\xa0'
    }

    render() {
        const section = this.props.section;
        const currentLevelPrefix = this.props.hierarchyLevel;
        if (section.children.length > 0) {
            const nextLevelPrefix = currentLevelPrefix + SectionOption.defaultProps.prefixElement;
            return <React.Fragment>
                    <option value={section.id}>{currentLevelPrefix}   {section.name}</option>
                    {section.children.map(section =>(
                        <SectionOption section={section} hierarchyLevel={nextLevelPrefix}/>
                    ))}
                </React.Fragment>;
        } else {
            return <option value={section.id}>{currentLevelPrefix}   {section.name}</option>;
        }
    }
}

const root = ReactDOM.createRoot(document.getElementById('pigeoner-sections'));
root.render(<SectionHierarchy/>);