//
//  DeleteUserViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-29.
//
import UIKit

class DeleteUserViewController: UIViewController {
    
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var successView: UIView!
    @IBOutlet weak var lbError: UILabel!
    @IBOutlet weak var lbSuccess: UILabel!
    @IBOutlet weak var btUserSelect: UIButton!
    
    private var users: [User] = []
    private var admin: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        refreshUserList()
    }
    
    func refreshUserList() {
        btUserSelect.isHidden = true
        indicator.isHidden = false
        let request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/user/get-all?except=\(admin!.username)&auth-user=\(admin!.username)")!)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) { data, response, _ in
            //TODO: Implement checking for the request response code
            if let data = data {
                
                DispatchQueue.main.async { [self] in
                    self.users = try! JSONDecoder().decode([User].self, from: data)
                    
                    var actions: [UIAction] = []
                    
                    for i in self.users.indices {
                        actions.append(UIAction(title: "\(users[i].name) (\(users[i].username))", handler: {action in
                            self.onChangeUser(i)
                        }))
                    }
                    
                    btUserSelect.menu = UIMenu(options: .singleSelection, children: actions)
                    btUserSelect.setTitle(actions.first?.title ?? "No Users Found", for: .normal)
                    //TODO: disable the submit button if there are no users to deleete
                    (btUserSelect.menu!.children.first as? UIAction)?.state = .on
                    btUserSelect.changesSelectionAsPrimaryAction = false
                    btUserSelect.showsMenuAsPrimaryAction = true
                    indicator.isHidden = true
                    btUserSelect.isHidden = false
                }
            }
        }.resume()
    }
    
    func with(_ adminUser: User) {
        admin = adminUser
    }
    
    func onChangeUser(_ senderIndex: Int) {
        (btUserSelect.menu!.children[senderIndex] as! UIAction).state = .on
        btUserSelect.setTitle((btUserSelect.menu!.children[senderIndex] as! UIAction).title, for: .normal)
    }
    
    @IBAction func onSubmit() {
        
    }
    
    @IBAction func onBack() {
        
    }
}
