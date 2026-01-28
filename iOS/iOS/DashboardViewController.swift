//
//  DashboardViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-26.
//

import UIKit

class DashboardViewController: UIViewController {
    
    private var user: User?
    
    @IBOutlet weak var lbUsername: UILabel!
    @IBOutlet weak var adminBadge: UIView!
    @IBOutlet weak var btAdministration: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        lbUsername.text = user!.username
        if(user!.administrator) {
            adminBadge.isHidden = false
            btAdministration.isHidden = false
        }
    }
    
    func with(_ initUser: User) {
        user = initUser
    }
    
    @IBAction func onSignOut() {
        let controller = UIAlertController(title: "Confirm Sign Out", message: "Are you sure you want to sign out?", preferredStyle: .alert)
        controller.addAction(UIAlertAction(title: "No", style: .cancel))
        controller.addAction(UIAlertAction(title: "Yes", style: .destructive) { _ in
            self.dismiss(animated: true)
        })
        present(controller, animated: true)
    }
    
    @IBAction func onAdministration() {
        performSegue(withIdentifier: "ShowAdminDashboard", sender: user)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "ShowAdminDashboard") {
            let destinationVC = segue.destination as! AdminDashboardViewController
            destinationVC.with(sender as! User)
        }
    }
}

